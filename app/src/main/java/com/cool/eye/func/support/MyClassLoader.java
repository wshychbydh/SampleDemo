package com.cool.eye.func.support;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    byte[] classData = loadClassData(name);
    if (classData == null) {
      throw new ClassNotFoundException();
    } else {
      return defineClass(name, classData, 0, classData.length);
    }
  }

  private byte[] loadClassData(String className) {
    String fileName = File.separatorChar
        + className.replace('.', File.separatorChar) + ".class";
    try {
      InputStream ins = new FileInputStream(fileName);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];
      int length = 0;
      while ((length = ins.read(buffer)) != -1) {
        baos.write(buffer, 0, length);
      }
      return baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
