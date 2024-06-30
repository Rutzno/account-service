package accountservice.dtos;

import accountservice.entities.MyUser;
import accountservice.entities.Payment;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.4
 */

public class UserPaymentDto {
    private String name;
    private String lastname;
    private String period;
    private String salary;

    public UserPaymentDto(MyUser myUser, Payment payment) {
        this.name = myUser.getFirstName();
        this.lastname = myUser.getLastName();
        this.period = convertPeriod(payment.getPeriod());
        this.salary = convertSalary(payment.getSalary());
    }

    private String convertPeriod(String period) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(period, inputFormat);
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMMM-yyyy"); //November-2020
        return yearMonth.format(outputFormat);
    }

    private String convertSalary(Long salary) {
        long dollars = salary / 100;
        long cents = salary % 100;
        return "%d dollar(s) %d cent(s)".formatted(dollars, cents);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}