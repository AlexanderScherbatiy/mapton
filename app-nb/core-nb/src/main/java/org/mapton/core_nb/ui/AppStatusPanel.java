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
package org.mapton.core_nb.ui;

import java.awt.BorderLayout;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javax.swing.JPanel;
import org.mapton.base.ui.StatusBarView;
import se.trixon.almond.nbp.fx.FxPanel;
import se.trixon.almond.util.swing.SwingHelper;

/**
 *
 * @author Patrik Karlström
 */
public class AppStatusPanel extends JPanel {

    private static AppStatusPanel sAppStatusPanel;
    private BorderPane mBorderPane;
    private final FxPanel mFxPanel;

    public static AppStatusPanel getInstance() {
        return sAppStatusPanel;
    }

    public AppStatusPanel() {
        sAppStatusPanel = this;
        mFxPanel = new FxPanel() {

            @Override
            protected void fxConstructor() {
                setScene(createScene());
            }

            private Scene createScene() {
                mBorderPane = new BorderPane();
                resetFx();
                return new Scene(mBorderPane);
            }
        };

        mFxPanel.initFx(null);
        mFxPanel.setPreferredSize(SwingHelper.getUIScaledDim(0, 32));

        setLayout(new BorderLayout());
        resetSwing();
    }

    public FxPanel getFxPanel() {
        return mFxPanel;
    }

    public void resetFx() {
        mBorderPane.setCenter(StatusBarView.getInstance());
    }

    public void resetSwing() {
        add(mFxPanel, BorderLayout.CENTER);
    }

}
