package de.thkoeln.swp.wawi.adminsteuerung.impl;

//imports
import de.thkoeln.swp.wawi.componentcontroller.services.CompType;
import de.thkoeln.swp.wawi.componentcontroller.services.IActivateComponent;


public class IActivateComponentImpl implements IActivateComponent {

    private boolean status = false;
    private int userid;

    public IActivateComponentImpl() {
    }

    /**
     *  CompType wird geliefert
     * @return CompType Admin
     */
    @Override
    public CompType getCompType() {

        return CompType.ADMIN;
    }

    /**
     * Komponente werden aktiviert und wenn das erfolgreich war dann Status auf aktiviert
     * @param userid ist die id des Admin
     */
    @Override
    public boolean activateComponent(int userid) {
        if(!status && userid == 14){
            status = true;
            return true;
        }
        else return false;

    }

    /**
     * Komponente werden deaktiviert und wenn das erfolgreich war dann Status auf deaktiviert
     */
    @Override
    public boolean deactivateComponent() {
        if (status) {
            status = false;
            return true;
        } else {
            return false;
        }
    }
    /**
     * Aktueller Status wird geliefert
     * Wenn Komponente aktiv sind dann return true
     */
    @Override
    public boolean isActivated()
    {
       return status;
    }

}
