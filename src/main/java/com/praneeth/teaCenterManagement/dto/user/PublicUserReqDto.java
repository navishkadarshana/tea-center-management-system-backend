package com.praneeth.teaCenterManagement.dto.user;



import lombok.*;
import lombok.experimental.SuperBuilder;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class PublicUserReqDto {

    private String firstName;
    private String lastName;
    private String address;
    private String mobile;
    private String nic;
    private Double latitude;
    private Double longitude;
    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private String bankBranch;

}
