package com.quyca.scriptwriter.ui.macros;

import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.SoundAction;

/**
 * The type Shared view model.
 */
public class MacroRecordFragmentViewModel extends ViewModel {
    private final MutableLiveData<SoundAction> activeSoundAction = new MutableLiveData<>();
    private final MutableLiveData<DocumentFile> tempFile = new MutableLiveData<>();

    public MutableLiveData<SoundAction> getActiveSoundAction() {
        if (activeSoundAction.getValue() == null) {
            activeSoundAction.setValue(null);
        }
        return activeSoundAction;
    }

    public void setActiveSoundAction(SoundAction sa) {
        activeSoundAction.setValue(sa);
    }

    public MutableLiveData<DocumentFile> getTempFile() {
        if (tempFile.getValue() == null) {
            tempFile.setValue(null);
        }
        return tempFile;
    }

    public void setTempFile(DocumentFile sa) {
        tempFile.setValue(sa);
    }
}