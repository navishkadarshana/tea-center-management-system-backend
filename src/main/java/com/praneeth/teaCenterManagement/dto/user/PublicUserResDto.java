package com.praneeth.teaCenterManagement.dto.user;


import com.praneeth.teaCenterManagement.enums.common.AccountVerifyStatus;
import com.praneeth.teaCenterManagement.enums.common.ActiveStatus;
import com.praneeth.teaCenterManagement.enums.common.UserRole;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class PublicUserResDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String nic;
    private String mobile;
    private Double latitude;
    private Double longitude;
    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private String bankBranch;


}
