package com.quyca.scriptwriter.config;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Representa una accion configurada para un robot actor en especifico. Generalmente se carga a partir de un archivo JSON de configuracion.
 *
 * @version 1.0
 * @since 1.0
 */
public class ConfiguredAction implements Serializable {
    /**
     * Representa el nombre de la accion
     */
    @Expose
    private String actionId;
    /**
     * Representa el nombre de la accion
     */
    @Expose
    private String actionName;
    /**
     * Representa los parametros por defecto de la accion.
     */
    @Expose
    private List<String> params;

    @Expose
    private int quycaId;

    /**
     * Crea una accion configurada.
     */
    public ConfiguredAction(FixedConfiguredAction action, String name,List<String> params) {
        this.actionId = action.name();
        this.actionName=name;
        this.params = params;
    }

    /**
     * Crea una accion configurada.
     */

    public ConfiguredAction(String actionId, List<String> params) {
        this.actionId = actionId;
        this.params = params;
    }

    /**
     * Instantiates a new Configured action.
     *
     * @param actionId the action id
     */
    public ConfiguredAction(String actionId) {
        this.actionId = actionId;
    }

    /**
     * Gets action id.
     *
     * @return action id
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets action id.
     *
     * @param actionId the action id
     */
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /**
     * Gets default params.
     *
     * @return default params
     */
    public List<String>getParams() {
        return params;
    }

    /**
     * Sets default params.
     *
     * @param params the default params
     */
    public void setParams(List<String> params) {
        this.params = params;
    }

    public int getQuycaId() {
        return quycaId;
    }

    public void setQuycaId(int quycaId) {
        this.quycaId = quycaId;
    }


    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String toString() {
        return "ConfiguredAction{" +
                "actionId='" + actionId + '\'' +
                '}';
    }
}
