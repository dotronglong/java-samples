package com.dotronglong.sample.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

public class Tail {
    private static final Logger logger = LoggerFactory.getLogger(Tail.class);

    /**
     * Read a number of lines from bottom of file
     * @see Tail https://en.wikipedia.org/wiki/Tail_(Unix)
     * @param path a string represents for path to file
     * @param numberOfLines an integer number indicates number of lines to be retrieved
     * @return a list of string represents for lines
     *
     * @throws InvalidPathException
     *         if file does not exist
     */
    public List<String> readLines(String path, int numberOfLines) {
        File file = new File(path);
        if (!file.exists()) {
            throw new InvalidPathException(path, "File does not exist");
        }

        return new ArrayList<String>();
    }


}
