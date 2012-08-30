package com.kp.malice.client.ui;

import java.util.Date;

import com.google.gwt.user.datepicker.client.DateBox;

public class UtcSafetyDateBox extends DateBox {

    public UtcSafetyDateBox() {
        super();
    }

    public Date getLocalPlus2HoursDate() {
        return new UtcSafetyOffsetDate(super.getValue()).getPlus2HoursDate();
    }

    private class UtcSafetyOffsetDate {

        private static final int UTC_SAFETY_OFFSET = 2 * 60 * 60 * 1000;
        private final Date originalDate;
        private final Date plus2HoursDate;

        private UtcSafetyOffsetDate(Date originalDate) {
            super();
            this.originalDate = originalDate;
            this.plus2HoursDate = convertLocalTimeToRoughlyUTC(originalDate);
        }

        private Date convertLocalTimeToRoughlyUTC(Date date) {
            Date secured = null;
            if (date != null) {
                Date d1 = new Date(date.getTime() + UTC_SAFETY_OFFSET);
                secured = d1;
            }
            return secured;
        }

        private Date getPlus2HoursDate() {
            return plus2HoursDate;
        }

        @Override
        public String toString() {
            return "UtcSafetyOffsetDate [originalDate=" + originalDate + ", plus2HoursDate=" + plus2HoursDate + "]";
        }
    }

}
