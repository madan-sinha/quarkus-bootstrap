package com.brightly;

import com.brightly.entity.User;
import com.brightly.repository.UserRepository;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserController {

    @Inject
    UserRepository userRepository;

    @POST
    @Transactional
    public Response create(User user) {
        userRepository.persist(user);
        return Response.ok(user).status(201).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        return Response.ok(User.findById(id)).status(200).build();
    }

    @GET
    public List<User> get() {
        return userRepository.listAll(Sort.by("id"));
    }
    @PUT
    @Path("{id}")
    @Transactional
    public User update(@PathParam("id") Long id, User user) {
        if (user.getUsername() == null) {
            throw new WebApplicationException("User Name was not set on request.", 422);
        }
        User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entity.setUsername(user.getUsername());
        userRepository.persist(entity);
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        userRepository.delete(entity);
        return Response.status(204).build();
    }
}
