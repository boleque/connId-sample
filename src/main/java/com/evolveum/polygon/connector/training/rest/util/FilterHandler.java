/*
 * Copyright (c) 2010-2023 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.polygon.connector.training.rest.util;

import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.*;

import java.util.Collection;
import java.util.List;

/**
 * Convenience class used for handling query events originating from IAM to the remote server. Class contains
 * constant fields representing filter operators (e.g eg represents the equals' operator, co the contains operator etc...)
 * Also constants representing characters dividing logical parts of the query string.
 */
public class FilterHandler implements FilterVisitor<String, String> {

    private static final Log LOG = Log.getLog(FilterHandler.class);

    private static final String EQUALS = " eq ";
    private static final String CONTAINS = " co ";
    private static final String STARTSWITH = " sw ";
    private static final String ENDSWITH = " ew ";

    // Note: Composite filter + logical negation
    private static final String AND = " and ";
    private static final String OR = " or ";
    private static final String NOT = " not ";

    private static final String LEFTPAR = "(";
    private static final String RIGHTPAR = ")";
    private static final String LEFTSQBRACKETS = "[";
    private static final String RIGHTSQBRACKETS = "]";

    private static final String ID = "id";

    private boolean expression = false;

    /**
     * Visitor method to process the logical composite AND filter.
     * @param s the string parameter which is being traversed during the query translation, it can be used
     * to hold the partially or fully translated filter string.
     * @param andFilter the filter object which is being translated
     */
    @Override
    public String visitAndFilter(String s, AndFilter andFilter) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s);
        if (!expression) {

            query.append(LEFTPAR).append(AND);
        } else {

            query.append(AND);
            expression = false;
        }
        Collection<Filter> filters = andFilter.getFilters();

        String processed = processCompositeFilter(filters, s);

        if (!processed.isEmpty()) {
            query.append(processed);
        } else {

        }

        if (expression) {

            query.append(RIGHTPAR);
        }

        query.append(RIGHTPAR);

        return query.toString();
    }

    /**
     * Visitor method to process the logical Contains filter.
     * @param s the string parameter which is being traversed during the query translation, it can be used
     * to hold the partially or fully translated filter string.
     * @param containsFilter the filter object which is being translated
     */
    @Override
    public String visitContainsFilter(String s, ContainsFilter containsFilter) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s).append(LEFTPAR);

        Attribute attr = containsFilter.getAttribute();

        String snippet = processStringFilter(attr, CONTAINS);

        if (snippet != null && !snippet.isEmpty()) {
            query.append(snippet);
        }

        query.append(RIGHTPAR);

        return query.toString();
    }

    @Override
    public String visitContainsAllValuesFilter(String s, ContainsAllValuesFilter containsAllValuesFilter) {
        LOG.warn("WARNING: Filter 'ContainsAllValuesFilter' not implemented by the resource, resulting query string will be NULL");
        return null;
    }

    /**
     * Visitor method to process the logical Equals filter.
     * @param s the string parameter which is being traversed during the query translation, it can be used
     * to hold the partially or fully translated filter string.
     * @param equalsFilter the filter object which is being translated
     */
    @Override
    public String visitEqualsFilter(String s, EqualsFilter equalsFilter) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s).append(LEFTPAR);

        Attribute attr = equalsFilter.getAttribute();

        String snippet = processStringFilter(attr, EQUALS);

        if (snippet != null && !snippet.isEmpty()) {
            query.append(snippet);
        }

        query.append(RIGHTPAR);

        return query.toString();
    }

    @Override
    public String visitExtendedFilter(String s, Filter filter) {
        LOG.warn("WARNING: Filter 'ExtendedFilter' not implemented by the resource, resulting query string will be NULL");

        return null;
    }

    @Override
    public String visitGreaterThanFilter(String s, GreaterThanFilter greaterThanFilter) {
        LOG.warn("WARNING: Filter 'GreaterThanFilter' not implemented by the resource, resulting query string will be NULL");

        return null;
    }

    @Override
    public String visitGreaterThanOrEqualFilter(String s, GreaterThanOrEqualFilter greaterThanOrEqualFilter) {
        LOG.warn("WARNING: Filter 'GreaterThanOrEqualFilter' not implemented by the resource, resulting query string will be NULL");

        return null;
    }

    @Override
    public String visitLessThanFilter(String s, LessThanFilter lessThanFilter) {
        LOG.warn("WARNING: Filter 'LessThanFilter' not implemented by the resource, resulting query string will be NULL");

        return null;
    }

    @Override
    public String visitLessThanOrEqualFilter(String s, LessThanOrEqualFilter lessThanOrEqualFilter) {
        LOG.warn("WARNING: Filter 'LessThanOrEqualFilter' not implemented by the resource, resulting query string will be NULL");

        return null;
    }

    /**
     * Visitor method to process the logical composite NOT filter.
     * @param s the string parameter which is being traversed during the query translation, it can be used
     * to hold the partially or fully translated filter string.
     * @param notFilter the filter object which is being translated
     */
    @Override
    public String visitNotFilter(String s, NotFilter notFilter) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s);
        Boolean wasInExpression = false;

        if (!expression) {

            query.append(LEFTPAR).append(NOT);

        } else {
            wasInExpression = true;
            query.append(NOT);

        }
        expression = true;

        Filter negatedFilter = notFilter.getFilter();
        Collection<Filter> filters = CollectionUtil.newList(negatedFilter);

        String processed = processCompositeFilter(filters, s);

        if (!processed.isEmpty()) {
            query.append(processed);
        } else {

        }

        if (!wasInExpression) {

            query.append(RIGHTPAR);
        }

        return query.toString();
    }

    /**
     * Visitor method to process the logical OR filter.
     * @param s the string parameter which is being traversed during the query translation, it can be used
     * to hold the partially or fully translated filter string.
     * @param orFilter the filter object which is being translated
     */
    @Override
    public String visitOrFilter(String s, OrFilter orFilter) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s);

        if (!expression) {

            query.append(LEFTPAR).append(OR);
        } else {

            query.append(OR);
            expression = false;
        }

        Collection<Filter> filters = orFilter.getFilters();

        String processed = processCompositeFilter(filters, s);

        if (!processed.isEmpty()) {
            query.append(processed);
        } else {

        }

        if (expression) {

            query.append(RIGHTPAR);
        }

        query.append(RIGHTPAR);

        return query.toString();
    }

    /**
     * Visitor method to process the logical Starts With filter.
     * @param s the string parameter which is being traversed during the query translation, it can be used
     * to hold the partially or fully translated filter string.
     * @param startsWithFilter the filter object which is being translated
     */
    @Override
    public String visitStartsWithFilter(String s, StartsWithFilter startsWithFilter) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s).append(LEFTPAR);

        Attribute attr = startsWithFilter.getAttribute();

        String snippet = processStringFilter(attr, STARTSWITH);

        if (snippet != null && !snippet.isEmpty()) {
            query.append(snippet);
        }

        query.append(RIGHTPAR);

        return query.toString();
    }

    /**
     * Visitor method to process the logical Ends With filter.
     * @param s the string parameter which is being traversed during the query translation, it can be used
     * to hold the partially or fully translated filter string.
     * @param endsWithFilter the filter object which is being translated
     */
    @Override
    public String visitEndsWithFilter(String s, EndsWithFilter endsWithFilter) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s).append(LEFTPAR);

        Attribute attr = endsWithFilter.getAttribute();

        String snippet = processStringFilter(attr, ENDSWITH);

        if (snippet != null && !snippet.isEmpty()) {
            query.append(snippet);
        }

        query.append(RIGHTPAR);

        return query.toString();
    }

    @Override
    public String visitEqualsIgnoreCaseFilter(String s, EqualsIgnoreCaseFilter equalsIgnoreCaseFilter) {
        LOG.warn("WARNING: Filter 'visitEqualsIgnoreCaseFilter' not implemented by the resource, resulting query string will be NULL");
        return null;
    }

    private String processStringFilter(Attribute attr, String operator) {

        StringBuilder query = new StringBuilder();

        if (attr != null) {
            String singleValue = null;
            String name = attr.getName();
            List value = attr.getValue();
            if (name != null && !name.isEmpty()) {

                if (Uid.NAME.equals(name)) {
                    name =ID;
                } else if (Name.NAME.equals(name)) {
                    name = TrainingRestConstants.OBJECT_ATTR_NAME;
                }
            } else {

            }
            if (value != null && !value.isEmpty()) {

                singleValue = AttributeUtil.getSingleValue(attr).toString();

            } else {

            }

            query.append(LEFTSQBRACKETS).append(name).append(RIGHTSQBRACKETS);
            query.append(operator);
            query.append(LEFTSQBRACKETS).append(singleValue).append(RIGHTSQBRACKETS);
        }

        return query.toString();
    }

    private String processCompositeFilter(Collection<Filter> filters, String s) {

        s = s != null ? s : "";

        StringBuilder query = new StringBuilder(s);

        for (Filter filter : filters) {

            if (!(filter instanceof CompositeFilter || filter instanceof NotFilter)) {

                if (!expression) {

                    query.append(LEFTPAR);
                    expression = true;
                } else {

                }

                query.append(filter.accept(this, s));
            } else {

                if (!expression) {

                    expression = true;
                } else {

                }

                query.append(filter.accept(this, s));
            }

        }

        return query.toString();
    }


}
