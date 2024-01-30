package com.jxm.yiti.domain;

import java.util.ArrayList;
import java.util.List;

public class RecruitInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RecruitInfoExample() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCompanyIsNull() {
            addCriterion("company is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIsNotNull() {
            addCriterion("company is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyEqualTo(String value) {
            addCriterion("company =", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotEqualTo(String value) {
            addCriterion("company <>", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyGreaterThan(String value) {
            addCriterion("company >", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyGreaterThanOrEqualTo(String value) {
            addCriterion("company >=", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLessThan(String value) {
            addCriterion("company <", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLessThanOrEqualTo(String value) {
            addCriterion("company <=", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyLike(String value) {
            addCriterion("company like", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotLike(String value) {
            addCriterion("company not like", value, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyIn(List<String> values) {
            addCriterion("company in", values, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotIn(List<String> values) {
            addCriterion("company not in", values, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyBetween(String value1, String value2) {
            addCriterion("company between", value1, value2, "company");
            return (Criteria) this;
        }

        public Criteria andCompanyNotBetween(String value1, String value2) {
            addCriterion("company not between", value1, value2, "company");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetIsNull() {
            addCriterion("recruitment_target is null");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetIsNotNull() {
            addCriterion("recruitment_target is not null");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetEqualTo(String value) {
            addCriterion("recruitment_target =", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetNotEqualTo(String value) {
            addCriterion("recruitment_target <>", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetGreaterThan(String value) {
            addCriterion("recruitment_target >", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetGreaterThanOrEqualTo(String value) {
            addCriterion("recruitment_target >=", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetLessThan(String value) {
            addCriterion("recruitment_target <", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetLessThanOrEqualTo(String value) {
            addCriterion("recruitment_target <=", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetLike(String value) {
            addCriterion("recruitment_target like", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetNotLike(String value) {
            addCriterion("recruitment_target not like", value, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetIn(List<String> values) {
            addCriterion("recruitment_target in", values, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetNotIn(List<String> values) {
            addCriterion("recruitment_target not in", values, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetBetween(String value1, String value2) {
            addCriterion("recruitment_target between", value1, value2, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andRecruitmentTargetNotBetween(String value1, String value2) {
            addCriterion("recruitment_target not between", value1, value2, "recruitmentTarget");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNull() {
            addCriterion("start_date is null");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNotNull() {
            addCriterion("start_date is not null");
            return (Criteria) this;
        }

        public Criteria andStartDateEqualTo(String value) {
            addCriterion("start_date =", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotEqualTo(String value) {
            addCriterion("start_date <>", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThan(String value) {
            addCriterion("start_date >", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThanOrEqualTo(String value) {
            addCriterion("start_date >=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThan(String value) {
            addCriterion("start_date <", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThanOrEqualTo(String value) {
            addCriterion("start_date <=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLike(String value) {
            addCriterion("start_date like", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotLike(String value) {
            addCriterion("start_date not like", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateIn(List<String> values) {
            addCriterion("start_date in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotIn(List<String> values) {
            addCriterion("start_date not in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateBetween(String value1, String value2) {
            addCriterion("start_date between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotBetween(String value1, String value2) {
            addCriterion("start_date not between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateIsNull() {
            addCriterion("distance_from_start_date is null");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateIsNotNull() {
            addCriterion("distance_from_start_date is not null");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateEqualTo(String value) {
            addCriterion("distance_from_start_date =", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateNotEqualTo(String value) {
            addCriterion("distance_from_start_date <>", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateGreaterThan(String value) {
            addCriterion("distance_from_start_date >", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateGreaterThanOrEqualTo(String value) {
            addCriterion("distance_from_start_date >=", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateLessThan(String value) {
            addCriterion("distance_from_start_date <", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateLessThanOrEqualTo(String value) {
            addCriterion("distance_from_start_date <=", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateLike(String value) {
            addCriterion("distance_from_start_date like", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateNotLike(String value) {
            addCriterion("distance_from_start_date not like", value, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateIn(List<String> values) {
            addCriterion("distance_from_start_date in", values, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateNotIn(List<String> values) {
            addCriterion("distance_from_start_date not in", values, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateBetween(String value1, String value2) {
            addCriterion("distance_from_start_date between", value1, value2, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromStartDateNotBetween(String value1, String value2) {
            addCriterion("distance_from_start_date not between", value1, value2, "distanceFromStartDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNull() {
            addCriterion("end_date is null");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNotNull() {
            addCriterion("end_date is not null");
            return (Criteria) this;
        }

        public Criteria andEndDateEqualTo(String value) {
            addCriterion("end_date =", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotEqualTo(String value) {
            addCriterion("end_date <>", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThan(String value) {
            addCriterion("end_date >", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThanOrEqualTo(String value) {
            addCriterion("end_date >=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThan(String value) {
            addCriterion("end_date <", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThanOrEqualTo(String value) {
            addCriterion("end_date <=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLike(String value) {
            addCriterion("end_date like", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotLike(String value) {
            addCriterion("end_date not like", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIn(List<String> values) {
            addCriterion("end_date in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotIn(List<String> values) {
            addCriterion("end_date not in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateBetween(String value1, String value2) {
            addCriterion("end_date between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotBetween(String value1, String value2) {
            addCriterion("end_date not between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateIsNull() {
            addCriterion("distance_from_end_date is null");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateIsNotNull() {
            addCriterion("distance_from_end_date is not null");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateEqualTo(String value) {
            addCriterion("distance_from_end_date =", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateNotEqualTo(String value) {
            addCriterion("distance_from_end_date <>", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateGreaterThan(String value) {
            addCriterion("distance_from_end_date >", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateGreaterThanOrEqualTo(String value) {
            addCriterion("distance_from_end_date >=", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateLessThan(String value) {
            addCriterion("distance_from_end_date <", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateLessThanOrEqualTo(String value) {
            addCriterion("distance_from_end_date <=", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateLike(String value) {
            addCriterion("distance_from_end_date like", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateNotLike(String value) {
            addCriterion("distance_from_end_date not like", value, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateIn(List<String> values) {
            addCriterion("distance_from_end_date in", values, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateNotIn(List<String> values) {
            addCriterion("distance_from_end_date not in", values, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateBetween(String value1, String value2) {
            addCriterion("distance_from_end_date between", value1, value2, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andDistanceFromEndDateNotBetween(String value1, String value2) {
            addCriterion("distance_from_end_date not between", value1, value2, "distanceFromEndDate");
            return (Criteria) this;
        }

        public Criteria andCityNatureIsNull() {
            addCriterion("city_nature is null");
            return (Criteria) this;
        }

        public Criteria andCityNatureIsNotNull() {
            addCriterion("city_nature is not null");
            return (Criteria) this;
        }

        public Criteria andCityNatureEqualTo(String value) {
            addCriterion("city_nature =", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureNotEqualTo(String value) {
            addCriterion("city_nature <>", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureGreaterThan(String value) {
            addCriterion("city_nature >", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureGreaterThanOrEqualTo(String value) {
            addCriterion("city_nature >=", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureLessThan(String value) {
            addCriterion("city_nature <", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureLessThanOrEqualTo(String value) {
            addCriterion("city_nature <=", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureLike(String value) {
            addCriterion("city_nature like", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureNotLike(String value) {
            addCriterion("city_nature not like", value, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureIn(List<String> values) {
            addCriterion("city_nature in", values, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureNotIn(List<String> values) {
            addCriterion("city_nature not in", values, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureBetween(String value1, String value2) {
            addCriterion("city_nature between", value1, value2, "cityNature");
            return (Criteria) this;
        }

        public Criteria andCityNatureNotBetween(String value1, String value2) {
            addCriterion("city_nature not between", value1, value2, "cityNature");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressIsNull() {
            addCriterion("deliver_address is null");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressIsNotNull() {
            addCriterion("deliver_address is not null");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressEqualTo(String value) {
            addCriterion("deliver_address =", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressNotEqualTo(String value) {
            addCriterion("deliver_address <>", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressGreaterThan(String value) {
            addCriterion("deliver_address >", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressGreaterThanOrEqualTo(String value) {
            addCriterion("deliver_address >=", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressLessThan(String value) {
            addCriterion("deliver_address <", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressLessThanOrEqualTo(String value) {
            addCriterion("deliver_address <=", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressLike(String value) {
            addCriterion("deliver_address like", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressNotLike(String value) {
            addCriterion("deliver_address not like", value, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressIn(List<String> values) {
            addCriterion("deliver_address in", values, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressNotIn(List<String> values) {
            addCriterion("deliver_address not in", values, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressBetween(String value1, String value2) {
            addCriterion("deliver_address between", value1, value2, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andDeliverAddressNotBetween(String value1, String value2) {
            addCriterion("deliver_address not between", value1, value2, "deliverAddress");
            return (Criteria) this;
        }

        public Criteria andExtrapolationIsNull() {
            addCriterion("extrapolation is null");
            return (Criteria) this;
        }

        public Criteria andExtrapolationIsNotNull() {
            addCriterion("extrapolation is not null");
            return (Criteria) this;
        }

        public Criteria andExtrapolationEqualTo(String value) {
            addCriterion("extrapolation =", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationNotEqualTo(String value) {
            addCriterion("extrapolation <>", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationGreaterThan(String value) {
            addCriterion("extrapolation >", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationGreaterThanOrEqualTo(String value) {
            addCriterion("extrapolation >=", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationLessThan(String value) {
            addCriterion("extrapolation <", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationLessThanOrEqualTo(String value) {
            addCriterion("extrapolation <=", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationLike(String value) {
            addCriterion("extrapolation like", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationNotLike(String value) {
            addCriterion("extrapolation not like", value, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationIn(List<String> values) {
            addCriterion("extrapolation in", values, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationNotIn(List<String> values) {
            addCriterion("extrapolation not in", values, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationBetween(String value1, String value2) {
            addCriterion("extrapolation between", value1, value2, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andExtrapolationNotBetween(String value1, String value2) {
            addCriterion("extrapolation not between", value1, value2, "extrapolation");
            return (Criteria) this;
        }

        public Criteria andImportantEventsIsNull() {
            addCriterion("important_events is null");
            return (Criteria) this;
        }

        public Criteria andImportantEventsIsNotNull() {
            addCriterion("important_events is not null");
            return (Criteria) this;
        }

        public Criteria andImportantEventsEqualTo(String value) {
            addCriterion("important_events =", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsNotEqualTo(String value) {
            addCriterion("important_events <>", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsGreaterThan(String value) {
            addCriterion("important_events >", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsGreaterThanOrEqualTo(String value) {
            addCriterion("important_events >=", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsLessThan(String value) {
            addCriterion("important_events <", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsLessThanOrEqualTo(String value) {
            addCriterion("important_events <=", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsLike(String value) {
            addCriterion("important_events like", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsNotLike(String value) {
            addCriterion("important_events not like", value, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsIn(List<String> values) {
            addCriterion("important_events in", values, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsNotIn(List<String> values) {
            addCriterion("important_events not in", values, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsBetween(String value1, String value2) {
            addCriterion("important_events between", value1, value2, "importantEvents");
            return (Criteria) this;
        }

        public Criteria andImportantEventsNotBetween(String value1, String value2) {
            addCriterion("important_events not between", value1, value2, "importantEvents");
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