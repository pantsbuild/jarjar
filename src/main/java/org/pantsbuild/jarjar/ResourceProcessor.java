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

import org.pantsbuild.jarjar.util.*;
import java.io.IOException;
import java.util.*;

class ResourceProcessor implements JarProcessor
{
    private PackageRemapper pr;

    public ResourceProcessor(PackageRemapper pr) {
        this.pr = pr;
    }

    public boolean process(EntryStruct struct) throws IOException {
        if (!struct.name.endsWith(".class"))
            struct.name = pr.mapPath(struct.name);
        return true;
    }
}
    
