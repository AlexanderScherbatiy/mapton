/*
 * Copyright 2020 Patrik Karlström.
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
package org.mapton.base.ui.poi;

import java.util.ArrayList;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.textfield.TextFields;
import org.mapton.api.MContextMenuItem;
import org.mapton.api.MDict;
import org.mapton.api.MPoi;
import org.mapton.api.MPoiManager;
import org.mapton.api.Mapton;
import static org.mapton.api.Mapton.getIconSizeToolBarInt;
import org.mapton.api.ui.MFilterPopOver;
import static org.mapton.api.ui.MFilterPopOver.GAP;
import static org.mapton.api.ui.MFilterPopOver.autoSize;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import se.trixon.almond.util.Dict;
import se.trixon.almond.util.SystemHelper;
import se.trixon.almond.util.fx.FxHelper;
import se.trixon.almond.util.icons.material.MaterialIcon;

/**
 *
 * @author Patrik Karlström
 */
public class PoisView extends BorderPane {

    private Menu mContextCopyMenu;
    private ContextMenu mContextMenu;
    private EventHandler<MouseEvent> mContextMenuMouseEvent;
    private Menu mContextOpenMenu;
    private FilterPopOver mFilterPopOver;
    private TextField mFilterTextField;
    private Label mItemCountLabel;
    private ListView<MPoi> mListView;
    private final MPoiManager mManager = MPoiManager.getInstance();

    public PoisView() {
        createUI();
        initListeners();
        populateContextProviders();

        mManager.refresh();
    }

    private void createUI() {
        mFilterPopOver = new FilterPopOver();

        mFilterTextField = TextFields.createClearableTextField();
        mFilterTextField.setPromptText(String.format("%s %s", Dict.SEARCH.toString(), MDict.POI.toString()));
        mFilterTextField.setMinWidth(20);

        mListView = new ListView<>();
        mListView.itemsProperty().bind(mManager.filteredItemsProperty());
        mListView.setCellFactory(param -> new PoiListCell());

        Action refreshAction = new Action(Dict.REFRESH.toString(), event -> {
            mManager.refresh();
        });
        refreshAction.setGraphic(MaterialIcon._Navigation.REFRESH.getImageView(getIconSizeToolBarInt()));

        Action filterAction = new Action(Dict.FILTER.toString(), event -> {
            if (mFilterPopOver.isShowing()) {
                mFilterPopOver.hide();
            } else {
                mFilterPopOver.show(((ButtonBase) event.getSource()));
            }
        });
        filterAction.setGraphic(MaterialIcon._Content.FILTER_LIST.getImageView(getIconSizeToolBarInt()));

        Action optionsAction = new Action(Dict.OPTIONS.toString(), event -> {
        });
        optionsAction.setGraphic(MaterialIcon._Action.SETTINGS.getImageView(getIconSizeToolBarInt()));
        optionsAction.setDisabled(true);

        ArrayList<Action> actions = new ArrayList<>();
        actions.add(refreshAction);
        actions.add(filterAction);
        actions.add(optionsAction);

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.HIDE);
        FxHelper.adjustButtonWidth(toolBar.getItems().stream(), getIconSizeToolBarInt());
        FxHelper.undecorateButtons(toolBar.getItems().stream());
        BorderPane topBorderPane = new BorderPane(mFilterTextField);
        topBorderPane.setRight(toolBar);
        toolBar.setMinWidth(getIconSizeToolBarInt() * 3 * 1.6);

        FxHelper.slimToolBar(toolBar);

        Label titleLabel = Mapton.createTitle(MDict.POI.toString());
        VBox topBox = new VBox(
                titleLabel,
                topBorderPane
        );
        mItemCountLabel = new Label();
        mItemCountLabel.setAlignment(Pos.BASELINE_RIGHT);
        setTop(topBox);
        setCenter(mListView);
        setBottom(mItemCountLabel);

        titleLabel.prefWidthProperty().bind(widthProperty());
        mItemCountLabel.prefWidthProperty().bind(widthProperty());

        mContextMenu = new ContextMenu(
                mContextCopyMenu = new Menu(MDict.COPY_LOCATION.toString()),
                mContextOpenMenu = new Menu(MDict.OPEN_LOCATION.toString())
        );
    }

    private void initListeners() {
        mFilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            mManager.refresh(newValue);
        });

        mManager.getFilteredItems().addListener((ListChangeListener.Change<? extends MPoi> c) -> {
            mItemCountLabel.setText(String.format("%d/%d", mManager.getFilteredItems().size(), mManager.getAllItems().size()));
        });

        Lookup.getDefault().lookupResult(MContextMenuItem.class).addLookupListener((LookupEvent ev) -> {
            populateContextProviders();
        });

        mContextMenuMouseEvent = mouseEvent -> {
            getScene().getWindow().requestFocus();
            mListView.requestFocus();
            MPoi poi = mListView.getSelectionModel().getSelectedItem();

            if (poi != null) {
                Mapton.getEngine().setLockedLatitude(poi.getLatitude());
                Mapton.getEngine().setLockedLongitude(poi.getLongitude());
                if (mouseEvent.isSecondaryButtonDown()) {
                    Mapton.getEngine().setLatitude(poi.getLatitude());
                    Mapton.getEngine().setLongitude(poi.getLongitude());

                    mContextMenu.show(this, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                } else if (mouseEvent.isPrimaryButtonDown()) {
                    mContextMenu.hide();
                }
            }
        };

        mListView.setOnMousePressed(mContextMenuMouseEvent);
    }

    private void populateContextProviders() {
        mContextCopyMenu.getItems().clear();
        mContextOpenMenu.getItems().clear();

        for (MContextMenuItem provider : Lookup.getDefault().lookupAll(MContextMenuItem.class)) {
            MenuItem item = new MenuItem(provider.getName());
            switch (provider.getType()) {
                case COPY:
                    mContextCopyMenu.getItems().add(item);
                    item.setOnAction((ActionEvent event) -> {
                        String s = provider.getUrl();
                        Mapton.getLog().v("Open location", s);
                        SystemHelper.copyToClipboard(s);
                    });
                    break;

                case OPEN:
                    mContextOpenMenu.getItems().add(item);
                    item.setOnAction((ActionEvent event) -> {
                        String s = provider.getUrl();
                        Mapton.getLog().v("Copy location", s);
                        SystemHelper.desktopBrowse(s);
                    });
                    break;
            }
        }

        mContextCopyMenu.getItems().sorted((MenuItem o1, MenuItem o2) -> o1.getText().compareToIgnoreCase(o2.getText()));
        mContextCopyMenu.setVisible(!mContextCopyMenu.getItems().isEmpty());

        mContextOpenMenu.getItems().sorted((MenuItem o1, MenuItem o2) -> o1.getText().compareToIgnoreCase(o2.getText()));
        mContextOpenMenu.setVisible(!mContextOpenMenu.getItems().isEmpty());
    }

    public class FilterPopOver extends MFilterPopOver {

        private PoiCategoryCheckTreeView mCategoryCheckTreeView;

        public FilterPopOver() {
            createUI();
            mCategoryCheckTreeView.populate();
        }

        private void createUI() {
            mCategoryCheckTreeView = new PoiCategoryCheckTreeView();
            VBox vBox = new VBox(GAP,
                    getButtonBox(),
                    new Separator(),
                    mCategoryCheckTreeView
            );

            autoSize(vBox);
            setContentNode(vBox);
        }

        @Override
        public void clear() {
            mCategoryCheckTreeView.getCheckModel().clearChecks();
        }

        @Override
        public void reset() {
            mCategoryCheckTreeView.getCheckModel().checkAll();
        }
    }

    class PoiListCell extends ListCell<MPoi> {

        private final Label mDesc1Label = new Label();
        private final Label mNameLabel = new Label();
        private final String mStyleBold = "-fx-font-weight: bold;";
        private VBox mVBox;

        public PoiListCell() {
            createUI();
        }

        @Override
        protected void updateItem(MPoi poi, boolean empty) {
            super.updateItem(poi, empty);
            if (poi == null || empty) {
                clearContent();
            } else {
                addContent(poi);
            }
        }

        private void addContent(MPoi poi) {
            setText(null);

            mNameLabel.setText(poi.getName());
            mDesc1Label.setText(String.format("%s: %s", poi.getProvider(), poi.getCategory()));
            setTooltip(new Tooltip(ToStringBuilder.reflectionToString(poi, ToStringStyle.MULTI_LINE_STYLE)));

            setGraphic(mVBox);
        }

        private void clearContent() {
            setText(null);
            setGraphic(null);
        }

        private void createUI() {
            mNameLabel.setStyle(mStyleBold);

            mVBox = new VBox(
                    mNameLabel,
                    mDesc1Label
            );
        }
    }
}
