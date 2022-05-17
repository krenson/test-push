package com.leforemhe.aem.site.core.models.pojo;

import javax.jcr.Binary;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Implementation of the Binary interface to write file into the CRX
 */
public class BinaryImpl implements Binary {
    private final byte[] byteArray;

    /**
     * Build a Binary based on a set of byte array
     * @param byteArray Array of byte to build the Binary
     */
    public BinaryImpl(byte[] byteArray) {
        this.byteArray = Arrays.copyOf(byteArray, byteArray.length);
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(byteArray);
    }

    @Override
    public int read(byte[] b, long position) throws IOException {
        if (getSize() <= position) {
            return -1;
        }
        try(InputStream stream = getStream()) {
            // Read/skip the next 'position' bytes ...
            long skip = position;
            while (skip > 0) {
                final long skipped = stream.skip(skip);
                if (skipped <= 0) {
                    return -1;
                }
                skip -= skipped;
            }
            return stream.read(b);
        }
    }

    @Override
    public long getSize() {
        return byteArray.length;
    }

    /**
     * This implementation of the Binary interface does not have any resource to close, so this method is not useful.
     * @deprecated
     */
    @Override
    @Deprecated
    public void dispose() {
        /*
         * No dispose behaviour for this implementation.
         */
    }
}
