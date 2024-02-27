package by.potapchuk.userservice.service.api;

public interface IUserPasswordEncoder {

    String encodePassword(String plainPassword);

    Boolean passwordMatches(String plainPassword, String encodedPassword);
}
