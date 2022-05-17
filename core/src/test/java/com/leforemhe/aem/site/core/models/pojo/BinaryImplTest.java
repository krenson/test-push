package com.leforemhe.aem.site.core.models.pojo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.RepositoryException;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("WeakerAccess")
public class BinaryImplTest {

    @Mock
    private ByteArrayInputStream byteArrayInputStream;
    @Mock
    private BinaryImpl binary;

    @Test
    public void getStreamTest() throws IOException {
        byte[] byteArray = new byte[]{1,2,3,4,5};
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BinaryImpl binary = new BinaryImpl(byteArray);
        copy(binary.getStream(), os);
        assertArrayEquals(byteArray, os.toByteArray());
    }

    @Test
    public void readTest() throws IOException, RepositoryException {
        byte[] byteArray = new byte[]{1,2,3,4,5};
        byte[] buffer = new byte[20];
        BinaryImpl binary = new BinaryImpl(byteArray);
        assertTrue(binary.read(buffer, 1)>0);
    }

    @Test
    public void readPositionTooFarTest() throws IOException, RepositoryException {
        byte[] byteArray = new byte[]{1,2,3,4,5};
        byte[] buffer = new byte[20];
        BinaryImpl binary = new BinaryImpl(byteArray);
        assertEquals(-1, binary.read(buffer, 50));
    }

    @Test
    public void readSkipAll() throws IOException, RepositoryException {
        lenient().when(binary.getStream()).thenReturn(byteArrayInputStream);
        lenient().when(binary.getSize()).thenReturn(5L);
        lenient().when(byteArrayInputStream.skip(anyLong())).thenReturn(-1L);
        lenient().when(binary.read(any(), anyLong())).thenCallRealMethod();
        byte[] buffer = new byte[20];
        assertEquals(-1, binary.read(buffer, 1));
        verify(byteArrayInputStream, atLeastOnce()).close();
    }

    @Test
    public void getSizeTest(){
        byte[] byteArray = new byte[]{1,2,3,4,5};
        byte[] buffer = new byte[20];
        BinaryImpl binary = new BinaryImpl(byteArray);
        assertEquals(byteArray.length, binary.getSize());
    }

    @Test
    public void disposeTest(){
        byte[] byteArray = new byte[]{1,2,3,4,5};
        BinaryImpl binary = new BinaryImpl(byteArray);
        try{
            binary.dispose();
        }catch (Exception e){
            fail(e);
        }
    }

    private void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }


}
