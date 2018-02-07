.. _spying:

Spying
======

In Spy mode, Hoverfly calls the real service if a request is not matched, otherwise, it will return the response from simulation.

.. code-block:: java

    try (Hoverfly hoverfly = new Hoverfly(configs(), SPY)) {

        hoverfly.start();
        hoverfly.importSimulation(classpath("simulation.json"));

        // do some requests here
    }
