package com.praneeth.teaCenterManagement.dto.auth;

import com.praneeth.teaCenterManagement.dto.user.PublicUserResDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;



@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class AdminUserAuthDto extends PublicUserResDto implements CommonUserAuth {
}
