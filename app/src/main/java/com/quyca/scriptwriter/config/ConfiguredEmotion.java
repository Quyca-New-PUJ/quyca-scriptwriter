package com.quyca.scriptwriter.config;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Representa una accion configurada para un robot actor en especifico. Generalmente se carga a partir de un archivo JSON de configuracion.
 *
 * @version 1.0
 * @since 1.0
 */
public class ConfiguredEmotion implements Serializable {
    @Expose
    private FixedConfiguredEmotion emotionId;
    @Expose
    private String emotionName;
    @Expose
    private float intensity;

    /**
     * Instantiates a new Configured emotion.
     *
     * @param emotionId the emotion id
     * @param name      the name
     * @param intensity the intensity that determines the emotional modulation.
     */
    public ConfiguredEmotion(FixedConfiguredEmotion emotionId, String name, float intensity) {
        this.emotionId = emotionId;
        this.emotionName = name;
        this.intensity = intensity;
    }

    /**
     * Gets emotion id.
     *
     * @return the emotion id
     */
    public FixedConfiguredEmotion getEmotionId() {
        return emotionId;
    }

    /**
     * Sets emotion id.
     *
     * @param emotionId the emotion id
     */
    public void setEmotionId(FixedConfiguredEmotion emotionId) {
        this.emotionId = emotionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguredEmotion that = (ConfiguredEmotion) o;
        return emotionId.equals(that.emotionId);
    }

    /**
     * Gets intensity.
     *
     * @return the intensity
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * Sets intensity.
     *
     * @param intensity the intensity
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    /**
     * Gets emotion name.
     *
     * @return the emotion name
     */
    public String getEmotionName() {
        return emotionName;
    }

    /**
     * Sets emotion name.
     *
     * @param emotionName the emotion name
     */
    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }

    @NonNull
    @Override
    public String toString() {
        return "ConfiguredEmotion{" +
                "emotionId='" + emotionId +
                "emotionName='" + emotionName +
                '}';
    }
}
