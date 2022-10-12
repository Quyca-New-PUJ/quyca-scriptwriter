package com.quyca.scriptwriter.config;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
    /**
     * Representa el id quyca
     */
    @Expose
    private int quycaId;
    /**
     * Representa si la accion necesita mensaje de respuesta o no.
     */
    @Expose
    private boolean answerable;
    /**
     * Representa los recursos usados por la accion..
     */
    @Expose
    private Set<String> usedResources;

    /**
     * Crea una accion configurada.
     *
     * @param action    the action
     * @param name      the name
     * @param params    the params
     * @param resources the resources
     */
    public ConfiguredAction(FixedConfiguredAction action, String name, List<String> params, Set<String> resources) {
        this.actionId = action.name();
        this.actionName = name;
        this.params = params;
        this.answerable = action.isAnswerable();
        this.usedResources = resources;
    }

    /**
     * Crea una accion configurada.
     *
     * @param actionId the action id
     * @param params   the parameters of the action
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
    public List<String> getParams() {
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

    /**
     * Gets quyca id.
     *
     * @return the quyca id
     */
    public int getQuycaId() {
        return quycaId;
    }

    /**
     * Sets quyca id.
     *
     * @param quycaId the quyca id
     */
    public void setQuycaId(int quycaId) {
        this.quycaId = quycaId;
    }


    /**
     * Gets action name.
     *
     * @return the action name
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Sets action name.
     *
     * @param actionName the action name
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Is answerable boolean.
     *
     * @return the boolean
     */
    public boolean isAnswerable() {
        return answerable;
    }


    /**
     * Gets used resources.
     *
     * @return the used resources
     */
    public Set<String> getUsedResources() {
        return usedResources;
    }

    /**
     * Sets used resources.
     *
     * @param usedResources the used resources
     */
    public void setUsedResources(Set<String> usedResources) {
        this.usedResources = usedResources;
    }

    @Override
    public String toString() {
        return "ConfiguredAction{" +
                "actionId='" + actionId + '\'' +
                ", actionName='" + actionName + '\'' +
                ", params=" + params +
                ", quycaId=" + quycaId +
                ", answerable=" + answerable +
                '}';
    }
}
