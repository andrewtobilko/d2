package com.tobilko;

import org.h2.bnf.RuleRepeat;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Andrew Tobilko on 10/28/18.
 */
public interface RuleRepository extends CrudRepository<Rule, Long> {

    Optional<Rule> findRuleByRole(String role);

}
