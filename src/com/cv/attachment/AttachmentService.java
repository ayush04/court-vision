/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.attachment;

import com.cv.utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
//import java.io.IOException;
//import java.io.OutputStream;
//import org.apache.commons.io.FileUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class AttachmentService {
    public static long saveAttachment(AttachmentData attachment) {
        long attachmentId = -1;
        Session session = null;
        Transaction tx = null;
        try {
            attachment.setIsDeleted(0);
            attachment.setWhenCreated(Utils.getCurrentTimestamp());
            session = Utils.getSessionFactory().openSession();
            tx = session.beginTransaction();
            attachmentId = (long) session.save(attachment);
            tx.commit();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return attachmentId;
    }

    public static AttachmentData getAttachment(long attachmentId) throws Exception {
        AttachmentData attachment = null;
        Session session = null;
        try {
            session = Utils.getSessionFactory().openSession();
            attachment = (AttachmentData) session.get(AttachmentData.class, attachmentId);
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (attachment != null && attachment.getIsDeleted() == 0) {
            return attachment;
        } else {
            throw new Exception("Attachment ID is not valid");
        }
    }
    
    public static AttachmentData getProfileImageOfUser(long userId) throws Exception {
        AttachmentData attachment = null;
        Session session = null;
        String query = "from AttachmentData where userId = " + userId + " and isDeleted = 0";
        try {
            session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            List<AttachmentData> attachmentList = session.createQuery(query).list();
            tx.commit();
            if(attachmentList != null && attachmentList.size() > 0) {
                attachment = attachmentList.get(0);
            }
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (attachment != null && attachment.getIsDeleted() == 0) {
            return attachment;
        } else {
            throw new Exception("USer ID is not valid");
        }
    }

    public static AttachmentData downloadAttachment(long attachmentId) throws Exception {
        Session session = null;
        AttachmentData attachment = null;
        try {
            session = Utils.getSessionFactory().openSession();
            attachment = (AttachmentData) session.get(AttachmentData.class, attachmentId);
        } catch (HibernateException e) {
            System.err.println("Internal server error : " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if(attachment != null && attachment.getIsDeleted() == 0) {
            return attachment;
        }
        else {
            throw new Exception("Attachment ID is not valid");
        }
    }
    
    public static void deleteAttachment(long attachmentId) throws Exception {
        Session session = null;
        if(attachmentId > 0) {
            try {
                session = Utils.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                AttachmentData aData = getAttachment(attachmentId);
                if (aData != null) {
                    aData.setIsDeleted(1);
                    aData.setWhenDeleted(Utils.getCurrentTimestamp());
                    session.update(aData);
                    tx.commit();
                }
            } 
            catch(HibernateException e) {
                System.out.println(e);
            }
            finally {
                if (session != null) {
                    session.close();
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
//        File inputFile = new File("C:\\Users\\Monalisa\\Desktop\\v.jpg");
//        System.err.println(inputFile.getName());
//        byte[] fileBytes = null;
//        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
//            fileBytes = new byte[(int) inputFile.length()];
//            inputStream.read(fileBytes);
//        } catch (Exception e) {
//            System.err.println(e);
//        }
//        AttachmentData attachment = new AttachmentData();
//        attachment.setAttachmentName(inputFile.getName());
//        attachment.setAttachmentFormat("JPEG");
//        attachment.setContent(fileBytes);
//        attachment.setUserId(1);
//        
//        AttachmentService.saveAttachment(attachment);
        
//        AttachmentData attachment = AttachmentService.downloadAttachment(1);
//        OutputStream out = null;
//        try {
//            FileUtils.writeByteArrayToFile(new File("C:\\Users\\Monalisa\\Desktop\\" + attachment.getAttachmentName()), attachment.getContent());
//        } catch (IOException e) {
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//            }
//        }

//        File inputFile = new File("C:\\Users\\Monalisa\\Desktop\\v.jpg");
//        System.out.println(inputFile.getName());
        //AttachmentService.deleteAttachment(1);
        System.out.println(AttachmentService.getProfileImageOfUser(1));
    }
}
