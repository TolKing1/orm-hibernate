package org.tolking.service;

public interface UserService {
    /**
     * Generates a new unique username based on the provided base username.
     * <p>
     * The method checks if the base username is already taken. If it is, it appends an incrementing
     * serial number to the base username to ensure uniqueness. The process continues until a unique
     * username is found.
     *
     * @param baseUsername the base username to be used for generating a new unique username.
     * @return the unique username that does not exist in the system.
     */
    String getNewUsername(String baseUsername);
}
