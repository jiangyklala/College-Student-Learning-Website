package com.jxm.yiti.domain;

import java.util.ArrayList;
import java.util.List;

public class GptInviterExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GptInviterExample() {
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

        public Criteria andInviterIdIsNull() {
            addCriterion("inviter_id is null");
            return (Criteria) this;
        }

        public Criteria andInviterIdIsNotNull() {
            addCriterion("inviter_id is not null");
            return (Criteria) this;
        }

        public Criteria andInviterIdEqualTo(Long value) {
            addCriterion("inviter_id =", value, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdNotEqualTo(Long value) {
            addCriterion("inviter_id <>", value, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdGreaterThan(Long value) {
            addCriterion("inviter_id >", value, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdGreaterThanOrEqualTo(Long value) {
            addCriterion("inviter_id >=", value, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdLessThan(Long value) {
            addCriterion("inviter_id <", value, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdLessThanOrEqualTo(Long value) {
            addCriterion("inviter_id <=", value, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdIn(List<Long> values) {
            addCriterion("inviter_id in", values, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdNotIn(List<Long> values) {
            addCriterion("inviter_id not in", values, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdBetween(Long value1, Long value2) {
            addCriterion("inviter_id between", value1, value2, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInviterIdNotBetween(Long value1, Long value2) {
            addCriterion("inviter_id not between", value1, value2, "inviterId");
            return (Criteria) this;
        }

        public Criteria andInvitedCountIsNull() {
            addCriterion("invited_count is null");
            return (Criteria) this;
        }

        public Criteria andInvitedCountIsNotNull() {
            addCriterion("invited_count is not null");
            return (Criteria) this;
        }

        public Criteria andInvitedCountEqualTo(Integer value) {
            addCriterion("invited_count =", value, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountNotEqualTo(Integer value) {
            addCriterion("invited_count <>", value, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountGreaterThan(Integer value) {
            addCriterion("invited_count >", value, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("invited_count >=", value, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountLessThan(Integer value) {
            addCriterion("invited_count <", value, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountLessThanOrEqualTo(Integer value) {
            addCriterion("invited_count <=", value, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountIn(List<Integer> values) {
            addCriterion("invited_count in", values, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountNotIn(List<Integer> values) {
            addCriterion("invited_count not in", values, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountBetween(Integer value1, Integer value2) {
            addCriterion("invited_count between", value1, value2, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInvitedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("invited_count not between", value1, value2, "invitedCount");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceIsNull() {
            addCriterion("invite_balance is null");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceIsNotNull() {
            addCriterion("invite_balance is not null");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceEqualTo(Integer value) {
            addCriterion("invite_balance =", value, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceNotEqualTo(Integer value) {
            addCriterion("invite_balance <>", value, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceGreaterThan(Integer value) {
            addCriterion("invite_balance >", value, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceGreaterThanOrEqualTo(Integer value) {
            addCriterion("invite_balance >=", value, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceLessThan(Integer value) {
            addCriterion("invite_balance <", value, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceLessThanOrEqualTo(Integer value) {
            addCriterion("invite_balance <=", value, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceIn(List<Integer> values) {
            addCriterion("invite_balance in", values, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceNotIn(List<Integer> values) {
            addCriterion("invite_balance not in", values, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceBetween(Integer value1, Integer value2) {
            addCriterion("invite_balance between", value1, value2, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andInviteBalanceNotBetween(Integer value1, Integer value2) {
            addCriterion("invite_balance not between", value1, value2, "inviteBalance");
            return (Criteria) this;
        }

        public Criteria andEarnRateIsNull() {
            addCriterion("earn_rate is null");
            return (Criteria) this;
        }

        public Criteria andEarnRateIsNotNull() {
            addCriterion("earn_rate is not null");
            return (Criteria) this;
        }

        public Criteria andEarnRateEqualTo(Integer value) {
            addCriterion("earn_rate =", value, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateNotEqualTo(Integer value) {
            addCriterion("earn_rate <>", value, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateGreaterThan(Integer value) {
            addCriterion("earn_rate >", value, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateGreaterThanOrEqualTo(Integer value) {
            addCriterion("earn_rate >=", value, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateLessThan(Integer value) {
            addCriterion("earn_rate <", value, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateLessThanOrEqualTo(Integer value) {
            addCriterion("earn_rate <=", value, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateIn(List<Integer> values) {
            addCriterion("earn_rate in", values, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateNotIn(List<Integer> values) {
            addCriterion("earn_rate not in", values, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateBetween(Integer value1, Integer value2) {
            addCriterion("earn_rate between", value1, value2, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarnRateNotBetween(Integer value1, Integer value2) {
            addCriterion("earn_rate not between", value1, value2, "earnRate");
            return (Criteria) this;
        }

        public Criteria andEarningsIsNull() {
            addCriterion("earnings is null");
            return (Criteria) this;
        }

        public Criteria andEarningsIsNotNull() {
            addCriterion("earnings is not null");
            return (Criteria) this;
        }

        public Criteria andEarningsEqualTo(Integer value) {
            addCriterion("earnings =", value, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsNotEqualTo(Integer value) {
            addCriterion("earnings <>", value, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsGreaterThan(Integer value) {
            addCriterion("earnings >", value, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsGreaterThanOrEqualTo(Integer value) {
            addCriterion("earnings >=", value, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsLessThan(Integer value) {
            addCriterion("earnings <", value, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsLessThanOrEqualTo(Integer value) {
            addCriterion("earnings <=", value, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsIn(List<Integer> values) {
            addCriterion("earnings in", values, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsNotIn(List<Integer> values) {
            addCriterion("earnings not in", values, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsBetween(Integer value1, Integer value2) {
            addCriterion("earnings between", value1, value2, "earnings");
            return (Criteria) this;
        }

        public Criteria andEarningsNotBetween(Integer value1, Integer value2) {
            addCriterion("earnings not between", value1, value2, "earnings");
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