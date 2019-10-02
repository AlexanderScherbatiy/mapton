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
package org.mapton.api;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 *
 * @author Patrik Karlström
 */
public abstract class MWorkbenchModule extends WorkbenchModule {

    protected final Logger LOGGER = Logger.getLogger(getClass().getName());
    protected final ObservableMap<KeyCombination, Runnable> mAccelerators;
    protected final HashSet<KeyCodeCombination> mKeyCodeCombinations;
    protected final MOptions2 mOptions2 = MOptions2.getInstance();
    protected final Preferences mPreferences;
    protected Stage mStage;
    private ResourceBundle mBundle;
    private final Scene mScene;

    public MWorkbenchModule(Scene scene, String name, Image icon) {
        super(name, icon);
        mScene = scene;
        mStage = (Stage) scene.getWindow();
        mKeyCodeCombinations = new HashSet<>();
        mAccelerators = mStage.getScene().getAccelerators();
        mPreferences = NbPreferences.forModule(getClass()).node(getClass().getCanonicalName());

        initListeners();
    }

    public ResourceBundle getBundle() {
        if (mBundle == null) {
            mBundle = NbBundle.getBundle(getClass());

        }

        return mBundle;
    }

    public String getBundleString(String key) {
        return getBundle().getString(key);
    }

    public Scene getScene() {
        return mScene;
    }

    public Stage getStage() {
        return (Stage) getWorkbench().getScene().getWindow();
    }

    public void postInit() {
        setNightMode(mOptions2.general().isNightMode());
    }

    public void setNightMode(boolean state) {
    }

    public void setTooltip(Control control, String string) {
        control.setTooltip(new Tooltip(string));
    }

    private void initListeners() {

        mOptions2.general().nightModeProperty().addListener((observable, oldValue, newValue) -> setNightMode(newValue));
    }

}
