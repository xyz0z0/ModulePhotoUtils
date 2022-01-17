package xyz.xyz0z0.photoutilstest;

import org.junit.Assert;
import org.junit.Test;

import xyz.xyz0z0.modulephotoutilstest.Book;

public class BookTest {

    @Test
    public void test() {
        Book book = new Book();
        int sum = book.testAdd(1, 2);
        Assert.assertEquals(sum, 3);
    }

}
