package com.example.sminstagram.repos.supabase;

import com.example.sminstagram.entities.supabase.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FollowRepo extends JpaRepository<Follow, UUID> {

}
