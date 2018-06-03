/*
 * Copyright 2018 Patrik Karlström.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.trixon.mapton.core.testing;

import org.controlsfx.control.action.Action;
import org.openide.util.lookup.ServiceProvider;
import se.trixon.almond.nbp.Almond;
import se.trixon.mapton.core.api.ToolActionProvider;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = ToolActionProvider.class)
public class Fx1MenuItem implements ToolActionProvider {

    @Override
    public Action getAction() {
        Action action = new Action("Fx1TopComp", (t) -> {
            Almond.openAndActivateTopComponent("Fx1TopComponent");
        });
        return action;
    }

    @Override
    public String getParent() {
        return "Test";
    }
}