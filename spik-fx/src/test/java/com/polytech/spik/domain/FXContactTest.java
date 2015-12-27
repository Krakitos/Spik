package com.polytech.spik.domain;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mfuntowicz on 27/12/15.
 */
public class FXContactTest extends TestCase {

    FXContact withImage;
    FXContact withoutImage;
    FXContact withoutName;
    FXContact withoutNameAndImage;

    public void setUp() throws Exception {
        super.setUp();

        withImage = new FXContact(1, "Test1", "blabla", new byte[0]);
        withoutImage = new FXContact(2, "Test2", "blabla");
        withoutName = new FXContact(3, "blabla", "blabla", new byte[0]);
        withoutNameAndImage = new FXContact(4, "blabla", "blabla");
    }

    @Test
    public void testImage(){
        Assert.assertTrue(withImage.hasPicture());
        Assert.assertFalse(withoutImage.hasPicture());
        Assert.assertTrue(withoutName.hasPicture());
        Assert.assertFalse(withoutNameAndImage.hasPicture());
    }

    @Test
    public void testName(){
        Assert.assertFalse(withImage.name().equals(withImage.address()));
        Assert.assertFalse(withoutImage.name().equals(withoutImage.address()));
        Assert.assertTrue(withoutName.name().equals(withoutName.address()));
        Assert.assertTrue(withoutNameAndImage.name().equals(withoutNameAndImage.address()));
    }
}