/**
 * Copyright 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pantsbuild.jarjar.util;

import java.io.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

abstract public class JarTransformer implements JarProcessor {

    public boolean process(EntryStruct struct) throws IOException {
        if (struct.name.endsWith(".class") && !struct.skipTransform) {
            ClassReader reader;
            try {
                reader = new ClassReader(struct.data);
            } catch (Exception e) {
                System.err.println("Unable to read bytecode from " + struct.name);
                e.printStackTrace();
                return true;
            }

            GetNameClassWriter w = new GetNameClassWriter(ClassWriter.COMPUTE_MAXS);
            try {
                reader.accept(transform(w), ClassReader.EXPAND_FRAMES);
            } catch (Exception e) {
                throw new Error("Unable to transform " + struct.name, e);
            }
            struct.data = w.toByteArray();
            struct.name = pathFromName(w.getClassName());
        }
        return true;
    }

    abstract protected ClassVisitor transform(ClassVisitor v);

    private static String pathFromName(String className) {
        return className.replace('.', '/') + ".class";
    }
}
