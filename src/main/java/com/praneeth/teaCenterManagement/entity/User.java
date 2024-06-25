package com.praneeth.teaCenterManagement.entity;


import com.praneeth.teaCenterManagement.enums.common.AccountVerifyStatus;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.enums.common.Gender;
import com.praneeth.teaCenterManagement.enums.common.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(nullable = true, unique = false)
    private String nic;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String userName;//email

    private String address;

    @Lob
    private String current_verify_token;

    private int filedLoginAttemptCount;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = true)
    private Double latitude;
    @Column(nullable = true)
    private Double longitude;

    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private String bankBranch;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date email_verify_link_timestamp;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogOutTimestamp;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @JsonFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Enumerated(value = EnumType.STRING)
    private ActiveStatus status;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Enumerated(value = EnumType.STRING)
    private AccountVerifyStatus account_verify_status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FarmerStocks> farmerStocks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Advance> advances = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FarmerOrder> farmerOrders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<BalancePayment> balancePayments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<PaymentHistory> paymentHistories = new ArrayList<>();



}
