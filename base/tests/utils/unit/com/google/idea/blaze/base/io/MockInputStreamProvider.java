/*
 * Copyright 2017 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.idea.blaze.base.io;

import static org.junit.Assert.fail;

import com.intellij.util.containers.HashMap;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Mocks {@link InputStreamProvider} for tests, providing an input stream corresponding to given
 * file contents without IO operations.
 */
public class MockInputStreamProvider implements InputStreamProvider {

  private final Map<String, byte[]> files = new HashMap<>();

  /** Add a file to provide an {@link InputStream} for, with specified contents. */
  public MockInputStreamProvider addFile(String filePath, String src) {
    try {
      addFile(filePath, src.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      fail(e.getMessage());
    }
    return this;
  }

  /** Add a file to provide an {@link InputStream} for, with specified contents. */
  public MockInputStreamProvider addFile(String filePath, byte[] contents) {
    files.put(filePath, contents);
    return this;
  }

  @Override
  public InputStream getFile(File path) throws FileNotFoundException {
    final byte[] contents = files.get(path.getPath());
    if (contents == null) {
      throw new FileNotFoundException(path + " has not been mapped into MockInputStreamProvider.");
    }
    return new ByteArrayInputStream(contents);
  }
}