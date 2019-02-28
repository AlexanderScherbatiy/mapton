/*
 * Copyright 2019 Patrik Karlström.
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
package org.mapton.worldwind.ruler;

import gov.nasa.worldwind.util.measure.MeasureTool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.mapton.worldwind.ModuleOptions;
import static org.mapton.worldwind.ModuleOptions.KEY_RULER_SHAPE;
import se.trixon.almond.util.Dict;

/**
 *
 * @author Patrik Karlström
 */
public class ShapePopOver extends BasePopOver {

    private final MeasureTool mMeasureTool;
    private final ModuleOptions mOptions = ModuleOptions.getInstance();

    public ShapePopOver(MeasureTool measureTool) {
        mMeasureTool = measureTool;
        setTitle(Dict.Geometry.GEOMETRY.toString());
        setContentNode(createUI());
    }

    private Node createUI() {
        String[] shapeTitles = {
            Dict.Geometry.LINE.toString(),
            Dict.Geometry.PATH.toString(),
            Dict.Geometry.POLYGON.toString(),
            Dict.Geometry.CIRCLE.toString(),
            Dict.Geometry.ELLIPSE.toString(),
            Dict.Geometry.SQUARE.toString(),
            Dict.Geometry.RECTANGLE.toString()
        };

        String shapes[] = {
            MeasureTool.SHAPE_LINE,
            MeasureTool.SHAPE_PATH,
            MeasureTool.SHAPE_POLYGON,
            MeasureTool.SHAPE_CIRCLE,
            MeasureTool.SHAPE_ELLIPSE,
            MeasureTool.SHAPE_SQUARE,
            MeasureTool.SHAPE_QUAD
        };

        RadioButton[] radioButtons = new RadioButton[shapeTitles.length];
        ToggleGroup toggleGroup = new ToggleGroup();

        EventHandler<ActionEvent> eventHandler = (ActionEvent t) -> {
            int index = toggleGroup.getToggles().indexOf(toggleGroup.getSelectedToggle());
            mMeasureTool.setMeasureShapeType(shapes[index]);
            mOptions.put(ModuleOptions.KEY_RULER_SHAPE, index);

            hide();
        };

        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new RadioButton(shapeTitles[i]);
            radioButtons[i].setToggleGroup(toggleGroup);
            radioButtons[i].setOnAction(eventHandler);
        }

        int index = mOptions.getInt(KEY_RULER_SHAPE);
        radioButtons[index].setSelected(true);
        mMeasureTool.setMeasureShapeType(shapes[index]);

        VBox vbox = new VBox(12, radioButtons);
        vbox.setPadding(new Insets(16));

        return vbox;
    }
}