/**
 * Copyright 2020 Dropbox
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

import org.junit.Test;
import org.pantsbuild.jarjar.util.EntryStruct;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class RenameProcessorTest
{
  @Test
  public void testRename() throws IOException {
    Rename rename = new Rename();
    rename.setSource("org/example/libnative.so");
    rename.setResult("com/example/shaded_libnative.so");
    RenameProcessor renameProcessor = new RenameProcessor(Collections.singletonList(rename));

    EntryStruct entryStruct = new EntryStruct();
    entryStruct.name = "org/example/libnative.so";
    assertTrue(renameProcessor.process(entryStruct));

    entryStruct.name = "com/example/shaded_libnative.so";
    assertTrue(renameProcessor.process(entryStruct));
  }
}
