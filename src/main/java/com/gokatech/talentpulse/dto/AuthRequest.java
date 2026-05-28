package com.gokatech.talentpulse.dto;

import com.gokatech.talentpulse.model.User;
import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String fullName;
    private User.UserRole role;
}
