/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.playerprofile;

import com.cv.exception.UserNotExistException;
import com.cv.utils.Utils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class ProfileService {
    private ProfileService() {}
    
    public static ProfileData getUserProfile(long userId) throws UserNotExistException {
        ProfileData userProfile = null;
        try {
            Session session = Utils.getSessionFactory().openSession();
            userProfile = (ProfileData) session.get(ProfileData.class, userId);
            session.close();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
        if(userProfile != null && userProfile.getIsDeleted() == 0) {
            return userProfile;
        }
        else {
            throw new UserNotExistException("User with user ID " + userId + " is deleted");
        }
    };
    
    public static void saveUserProfile(ProfileData userProfile) {
        try {
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(userProfile);
            session.getTransaction().commit();
            session.close();
        }
        catch(HibernateException e) {
            System.err.println("Exception : " + e);
        }
    };
    
    public static void updateUserProfile(ProfileData userProfile) throws UserNotExistException {
        if(userProfile.getUserId() > 0) {
            Session session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            ProfileData profileData = getUserProfile(userProfile.getUserId());
            if(profileData != null) {
                if(userProfile.getBasketsScored() > -1) {
                    profileData.setBasketsScored(userProfile.getBasketsScored());
                }
                if(userProfile.getFitbitClientId() != null) {
                    profileData.setFitbitClientId(userProfile.getFitbitClientId());
                }
                if(userProfile.getFitbitEnabled() > -1) {
                    profileData.setFitbitEnabled(userProfile.getFitbitEnabled());
                }
                if(userProfile.getHeight() > 0) {
                    profileData.setHeight(userProfile.getHeight());
                }
                if(userProfile.getRole() != null) {
                    profileData.setRole(userProfile.getRole());
                }
                if(userProfile.getTotalGamesPlayed() > 0) {
                    profileData.setTotalGamesPlayed(userProfile.getTotalGamesPlayed());
                }
                if(userProfile.getWeight() > 0) {
                    profileData.setWeight(userProfile.getWeight());
                }
                profileData.setWhenModified(Utils.getCurrentTimestamp());
                session.update(profileData);
                tx.commit();
                session.close();
            }
        }
        else {
            throw new UserNotExistException("Invalid user ID : " + userProfile.getUserId());
        }
    };
    
    public static void deleteUserProfile(long userId) throws UserNotExistException {
        if(userId > 0) {
            Session session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            ProfileData profileData = getUserProfile(userId);
            if(profileData != null) {
                profileData.setIsDeleted(1);
                profileData.setWhenDeleted(Utils.getCurrentTimestamp());
                session.update(profileData);
                tx.commit();
                session.close();
            }
            else {
                throw new UserNotExistException("User " + userId + " does not exist");
            }
        }
        else {
            throw new UserNotExistException("User " + userId + " does not exist");
        }
    };
    
    public static void updateBasketsScored(long userId, int baskets) throws UserNotExistException {
        if(userId > 0) {
            Session session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            ProfileData profileData = getUserProfile(userId);
            if(profileData != null) {
                profileData.setBasketsScored(profileData.getBasketsScored() + baskets);
                session.update(profileData);
                tx.commit();
                session.close();
            }
        }
        else {
            throw new UserNotExistException("User " + userId + " does not exist");
        }
    }
    
    public static void main(String[] args) {
        ProfileData profile = new ProfileData();
        
        profile.setUserId(1);
        profile.setBasketsScored(10);
        profile.setHeight(150);
        profile.setWeight(89);
        ProfileService.saveUserProfile(profile);
    }
}
