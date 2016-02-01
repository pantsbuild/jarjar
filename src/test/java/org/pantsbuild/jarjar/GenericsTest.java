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

package org.pantsbuild.jarjar;

import java.util.Arrays;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.pantsbuild.jarjar.util.RemappingClassTransformer;

import junit.framework.TestCase;

public class GenericsTest
extends TestCase
{
    @Test
    public void testTransform() throws Exception {
         Rule rule = new Rule();
         rule.setPattern("java.lang.String");
         rule.setResult("com.tonicsystems.String");
         RemappingClassTransformer t = new RemappingClassTransformer(new PackageRemapper(Arrays.asList(rule), false));
         t.setTarget(new EmptyClassVisitor());
         ClassReader reader = new ClassReader(getClass().getResourceAsStream("/Generics.class"));
         reader.accept(t, 0);
    }
}
