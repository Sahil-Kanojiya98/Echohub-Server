package com.app.Echohub.repository;

import com.app.Echohub.dto.UserDescResponse;
import com.app.Echohub.dto.UserProfileDTO;
import com.app.Echohub.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {


    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Aggregation(pipeline = {
            "{ $sample: { size: 6 } }",
            "{ $project: { 'id': 1, 'username': 1, 'fullname': 1, 'profilePictureUrl': 1 } }"
    })
    List<UserDescResponse> findRandomUsers();

    @Aggregation(pipeline = {
            "{ $match: { '_id': ?0 } }",
            "{ $project: { " +
                    "'id': '$_id', " +
                    "'username': 1, " +
                    "'fullname': 1, " +
                    "'email': 1, " +
                    "'profile_picture_url': 1, " +
                    "'cover_picture_url': 1, " +
                    "'bio': 1, " +
                    "'link': 1, " +
                    "'created_at': 1, " +
                    "'followersCount': { $size: '$followers' }, " +
                    "'followingsCount': { $size: '$followings' }, " +
                    "'postsCount': { $size: '$posts' }, " +
                    "'is_following': { $in: [?1, '$followers'] } " +
                    "} }"
    })
    UserProfileDTO findUserProfileById(String userId, String myId);

//    @Query("{ '$or': [ { 'fullname': { '$regex': ?0, '$options': 'i' } }, { 'username': { '$regex': ?0, '$options': 'i' } } ] }")
//    List<User> searchByPattern(String pattern);

//    @Query(value = "{ '$or': [ { 'fullname': { '$regex': ?0, '$options': 'i' } }, { 'username': { '$regex': ?0, '$options': 'i' } } ] }",
//            fields = "{ 'id': '$_id', 'username': 1, 'fullname': 1, 'profile_picture_url': 1 }")
//    Page<User> searchByPattern(String pattern, Pageable pageable);

    @Query(value = "{ '$or': [ { 'fullname': { '$regex': ?0, '$options': 'i' } }, { 'username': { '$regex': ?0, '$options': 'i' } } ], 'roles': { '$in': ['ROLE_USER'] } }",
            fields = "{ 'id': '$_id', 'username': 1, 'fullname': 1, 'profile_picture_url': 1 }")
    Page<User> searchByPattern(String pattern, Pageable pageable);

}
