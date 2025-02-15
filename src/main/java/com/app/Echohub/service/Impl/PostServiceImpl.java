package com.app.Echohub.service.Impl;

import com.app.Echohub.dto.PostResponseDTO;
import com.app.Echohub.exceptions.EntityNotFoundException;
import com.app.Echohub.model.Post;
import com.app.Echohub.model.User;
import com.app.Echohub.repository.PostRepository;
import com.app.Echohub.repository.UserRepository;
import com.app.Echohub.service.PostService;
import com.app.Echohub.utility.FileStorage;
import com.app.Echohub.utility.enums.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String makePost(User user, String content, MultipartFile imageFile, MultipartFile videoFile) {
        String imageUrl = null;
        String videoUrl = null;
        try{
            if (imageFile!=null){
                imageUrl = FileStorage.saveFile(imageFile, FileType.POST);
            }else if(videoFile !=null){
                videoUrl = FileStorage.saveFile(videoFile, FileType.POST);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occured while posting image or video");
        }
        Post post = Post.builder()
                .user(user)
                .content(content)
                .imageUrl(imageUrl)
                .videoUrl(videoUrl)
                .build();
        Post PersistedPost = postRepository.save(post);
        System.out.println(post);
        user.getPosts().add(PersistedPost.getId());
        userRepository.save(user);
        return PersistedPost.getId();
    }

    @Override
    public List<PostResponseDTO> findRandomPosts(User user,int number) {
        return postRepository.findRandomPostsWithUserDetails(user.getId(),number);
    }

    @Override
    public void deletePost(User user, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post not found"));
        if (!post.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("you not allowed to delete this post");
        }else {
            System.out.println(user);
            user.getPosts().remove(post.getId());
            System.out.println(user);
            postRepository.delete(post);
            userRepository.save(user);
            System.out.println("ended deletion");
        }
    }

    @Override
    public void likePost(User user, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post not found!"));
        if (!post.getLikes().contains(user.getId())){
            post.getLikes().add(user.getId());
            postRepository.save(post);
        }else{
            throw new IllegalStateException("already liked");
        }
    }

    @Override
    public void unlikePost(User user, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post not found!"));
        if (post.getLikes().contains(user.getId())){
            post.getLikes().remove(user.getId());
            postRepository.save(post);
        }
        else {
            throw new IllegalStateException("not liked to unlike");
        }
    }

}
