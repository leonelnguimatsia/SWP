package de.thkoeln.swp.wawi.wawidbmodel.exceptions;

public class NoEntityManagerException extends RuntimeException
{
    public NoEntityManagerException()
    {
        super("Kein EntityManager gesetzt");
    }
}
