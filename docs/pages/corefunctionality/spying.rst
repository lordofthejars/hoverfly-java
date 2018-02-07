.. _spying:

Spying
======

In Spy mode, Hoverfly calls the real service if a request is not matched, otherwise, it simulates.

This mode allows you to spy on some of the endpoints of the services your test depends on, while letting other traffic to pass through.

.. code-block:: java

    try (Hoverfly hoverfly = new Hoverfly(configs(), SPY)) {

        hoverfly.start();
        hoverfly.importSimulation(classpath("simulation.json"));

        // do some requests here
    }
