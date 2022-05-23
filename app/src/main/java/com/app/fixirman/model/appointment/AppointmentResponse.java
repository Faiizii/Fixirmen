
package com.app.fixirman.model.appointment;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("appointments")
    @Expose
    private List<Appointment> appointments = null;
    @SerializedName("appointment")
    @Expose
    private Appointment appointment;
    @SerializedName("coupon_id")
    @Expose
    private Integer couponId;

    public final static Creator<AppointmentResponse> CREATOR = new Creator<AppointmentResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AppointmentResponse createFromParcel(Parcel in) {
            return new AppointmentResponse(in);
        }

        public AppointmentResponse[] newArray(int size) {
            return (new AppointmentResponse[size]);
        }

    }
    ;

    protected AppointmentResponse(Parcel in) {
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.couponId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.appointments, (Appointment.class.getClassLoader()));
        this.appointment = ((Appointment) in.readValue((Appointment.class.getClassLoader())));
    }

    public AppointmentResponse() {
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(couponId);
        dest.writeValue(message);
        dest.writeList(appointments);
        dest.writeValue(appointment);
    }

    public int describeContents() {
        return  0;
    }
}
