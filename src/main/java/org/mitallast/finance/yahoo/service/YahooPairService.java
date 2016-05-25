package org.mitallast.finance.yahoo.service;

import org.mitallast.finance.controller.TickerCsvController;
import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.repository.YahooPairRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class YahooPairService extends AsyncService {

    private final static Logger logger = LoggerFactory.getLogger(TickerCsvController.class);

    @Autowired
    private YahooPairDataService pairDataService;

    @Autowired
    private YahooPairRepository pairRepository;

    @Autowired
    private Validator validator;

    public List<Future<YahooPair>> indexPair() {
        List<YahooPair> pairList = pairRepository.findAll();
        List<Future<YahooPair>> futureList = new ArrayList<>(pairList.size());
        for (YahooPair pair : pairList) {
            Future<YahooPair> future = indexPair(pair);
            futureList.add(future);
        }
        return futureList;
    }

    public Future<YahooPair> indexPair(final YahooPair pair) {
        return pool.submit(new Callable<YahooPair>() {

            @Override
            public YahooPair call() throws Exception {
                try {
                    YahooPair updatePair = null;
                    if (pair.isNew()) {
                        updatePair = pairRepository.findOneByLeftAndRight(pair.getLeft(), pair.getRight());

                    }
                    if (updatePair == null) {
                        updatePair = pair;
                    }
                    pairDataService.update(updatePair);
                    Set<ConstraintViolation<YahooPair>> violations = validator.validate(updatePair);
                    if (!violations.isEmpty()) {
                        logger.info("pair {}", updatePair);
                        logger.info("violations {}", violations);
                    } else {
                        logger.info("updatePair {}", updatePair);
                        pairRepository.save(updatePair);
                    }
                    return updatePair;
                } catch (Throwable e) {
                    logger.error("error update pair", e);
                    throw e;
                }
            }
        });
    }

    public Future<Set<YahooPair>> indexPair(final YahooIndustry industry) {
        return pool.submit(new Callable<Set<YahooPair>>() {

            @Override
            public Set<YahooPair> call() throws Exception {
                try {
                    return doCall();
                } catch (Throwable e) {
                    logger.error("error index pair", e);
                    throw e;
                }
            }

            private Set<YahooPair> doCall() throws Exception {
                logger.debug("get pairs");
                Set<YahooPair> pairNew = pairDataService.getPairs(industry);

                logger.debug("get old pairs");
                List<YahooPair> pairOld = pairRepository.findAllByIndustry(industry);
                Map<String, YahooPair> oldMap = new HashMap<>(pairOld.size());
                for (YahooPair pair : pairOld) {
                    oldMap.put(pair.getTitle(), pair);
                }

                logger.debug("replace tasks with old pair");
                Set<YahooPair> pairSet = new HashSet<>(pairNew.size());
                for (YahooPair newPair : pairNew) {
                    YahooPair oldPair = oldMap.get(newPair.getTitle());
                    if (oldPair != null) {
                        pairOld.remove(oldPair);
                        pairSet.add(oldPair);
                    } else {
                        pairSet.add(newPair);
                    }
                }

                logger.debug("update pairs");
                ArrayList<Future<YahooPair>> tasks = new ArrayList<>(pairSet.size());
                for (YahooPair pair : pairSet) {
                    tasks.add(pairDataService.updatePair(pair));
                }
                Set<YahooPair> updatedPairSet = new HashSet<>(pairSet.size());
                for (Future<YahooPair> task : tasks) {
                    YahooPair pair = task.get();
                    Set<ConstraintViolation<YahooPair>> violations = validator.validate(pair);
                    if (!violations.isEmpty()) {
                        logger.info("pair {}", pair);
                        logger.info("violations {}", violations);
                        if (pair.getId() > 0) {
                            pairOld.add(pair);
                        }
                    } else {
                        updatedPairSet.add(pair);
                    }
                }

                logger.debug("save pairs");
                pairRepository.delete(pairOld);
                pairRepository.save(updatedPairSet);
                return updatedPairSet;
            }
        });
    }
}
