package com.jxm.yiti.domain;

import java.util.ArrayList;
import java.util.List;

public class ChatRecordInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChatRecordInfoExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("`date` is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("`date` is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(String value) {
            addCriterion("`date` =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(String value) {
            addCriterion("`date` <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(String value) {
            addCriterion("`date` >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(String value) {
            addCriterion("`date` >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(String value) {
            addCriterion("`date` <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(String value) {
            addCriterion("`date` <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLike(String value) {
            addCriterion("`date` like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotLike(String value) {
            addCriterion("`date` not like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<String> values) {
            addCriterion("`date` in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<String> values) {
            addCriterion("`date` not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(String value1, String value2) {
            addCriterion("`date` between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(String value1, String value2) {
            addCriterion("`date` not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andNtimesIsNull() {
            addCriterion("ntimes is null");
            return (Criteria) this;
        }

        public Criteria andNtimesIsNotNull() {
            addCriterion("ntimes is not null");
            return (Criteria) this;
        }

        public Criteria andNtimesEqualTo(String value) {
            addCriterion("ntimes =", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesNotEqualTo(String value) {
            addCriterion("ntimes <>", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesGreaterThan(String value) {
            addCriterion("ntimes >", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesGreaterThanOrEqualTo(String value) {
            addCriterion("ntimes >=", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesLessThan(String value) {
            addCriterion("ntimes <", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesLessThanOrEqualTo(String value) {
            addCriterion("ntimes <=", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesLike(String value) {
            addCriterion("ntimes like", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesNotLike(String value) {
            addCriterion("ntimes not like", value, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesIn(List<String> values) {
            addCriterion("ntimes in", values, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesNotIn(List<String> values) {
            addCriterion("ntimes not in", values, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesBetween(String value1, String value2) {
            addCriterion("ntimes between", value1, value2, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtimesNotBetween(String value1, String value2) {
            addCriterion("ntimes not between", value1, value2, "ntimes");
            return (Criteria) this;
        }

        public Criteria andNtokensIsNull() {
            addCriterion("ntokens is null");
            return (Criteria) this;
        }

        public Criteria andNtokensIsNotNull() {
            addCriterion("ntokens is not null");
            return (Criteria) this;
        }

        public Criteria andNtokensEqualTo(String value) {
            addCriterion("ntokens =", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensNotEqualTo(String value) {
            addCriterion("ntokens <>", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensGreaterThan(String value) {
            addCriterion("ntokens >", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensGreaterThanOrEqualTo(String value) {
            addCriterion("ntokens >=", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensLessThan(String value) {
            addCriterion("ntokens <", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensLessThanOrEqualTo(String value) {
            addCriterion("ntokens <=", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensLike(String value) {
            addCriterion("ntokens like", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensNotLike(String value) {
            addCriterion("ntokens not like", value, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensIn(List<String> values) {
            addCriterion("ntokens in", values, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensNotIn(List<String> values) {
            addCriterion("ntokens not in", values, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensBetween(String value1, String value2) {
            addCriterion("ntokens between", value1, value2, "ntokens");
            return (Criteria) this;
        }

        public Criteria andNtokensNotBetween(String value1, String value2) {
            addCriterion("ntokens not between", value1, value2, "ntokens");
            return (Criteria) this;
        }

        public Criteria andVtimesIsNull() {
            addCriterion("vtimes is null");
            return (Criteria) this;
        }

        public Criteria andVtimesIsNotNull() {
            addCriterion("vtimes is not null");
            return (Criteria) this;
        }

        public Criteria andVtimesEqualTo(String value) {
            addCriterion("vtimes =", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesNotEqualTo(String value) {
            addCriterion("vtimes <>", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesGreaterThan(String value) {
            addCriterion("vtimes >", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesGreaterThanOrEqualTo(String value) {
            addCriterion("vtimes >=", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesLessThan(String value) {
            addCriterion("vtimes <", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesLessThanOrEqualTo(String value) {
            addCriterion("vtimes <=", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesLike(String value) {
            addCriterion("vtimes like", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesNotLike(String value) {
            addCriterion("vtimes not like", value, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesIn(List<String> values) {
            addCriterion("vtimes in", values, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesNotIn(List<String> values) {
            addCriterion("vtimes not in", values, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesBetween(String value1, String value2) {
            addCriterion("vtimes between", value1, value2, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtimesNotBetween(String value1, String value2) {
            addCriterion("vtimes not between", value1, value2, "vtimes");
            return (Criteria) this;
        }

        public Criteria andVtokensIsNull() {
            addCriterion("vtokens is null");
            return (Criteria) this;
        }

        public Criteria andVtokensIsNotNull() {
            addCriterion("vtokens is not null");
            return (Criteria) this;
        }

        public Criteria andVtokensEqualTo(String value) {
            addCriterion("vtokens =", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensNotEqualTo(String value) {
            addCriterion("vtokens <>", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensGreaterThan(String value) {
            addCriterion("vtokens >", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensGreaterThanOrEqualTo(String value) {
            addCriterion("vtokens >=", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensLessThan(String value) {
            addCriterion("vtokens <", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensLessThanOrEqualTo(String value) {
            addCriterion("vtokens <=", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensLike(String value) {
            addCriterion("vtokens like", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensNotLike(String value) {
            addCriterion("vtokens not like", value, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensIn(List<String> values) {
            addCriterion("vtokens in", values, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensNotIn(List<String> values) {
            addCriterion("vtokens not in", values, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensBetween(String value1, String value2) {
            addCriterion("vtokens between", value1, value2, "vtokens");
            return (Criteria) this;
        }

        public Criteria andVtokensNotBetween(String value1, String value2) {
            addCriterion("vtokens not between", value1, value2, "vtokens");
            return (Criteria) this;
        }

        public Criteria andIvtimesIsNull() {
            addCriterion("iVtimes is null");
            return (Criteria) this;
        }

        public Criteria andIvtimesIsNotNull() {
            addCriterion("iVtimes is not null");
            return (Criteria) this;
        }

        public Criteria andIvtimesEqualTo(String value) {
            addCriterion("iVtimes =", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesNotEqualTo(String value) {
            addCriterion("iVtimes <>", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesGreaterThan(String value) {
            addCriterion("iVtimes >", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesGreaterThanOrEqualTo(String value) {
            addCriterion("iVtimes >=", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesLessThan(String value) {
            addCriterion("iVtimes <", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesLessThanOrEqualTo(String value) {
            addCriterion("iVtimes <=", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesLike(String value) {
            addCriterion("iVtimes like", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesNotLike(String value) {
            addCriterion("iVtimes not like", value, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesIn(List<String> values) {
            addCriterion("iVtimes in", values, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesNotIn(List<String> values) {
            addCriterion("iVtimes not in", values, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesBetween(String value1, String value2) {
            addCriterion("iVtimes between", value1, value2, "ivtimes");
            return (Criteria) this;
        }

        public Criteria andIvtimesNotBetween(String value1, String value2) {
            addCriterion("iVtimes not between", value1, value2, "ivtimes");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}