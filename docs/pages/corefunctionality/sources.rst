.. _sources:

Simulation source
=================

There are a few different potential sources for Simulations:

.. code-block:: java

    SimulationSource.classpath("simulation.json"); //classpath
    SimulationSource.defaultPath("simulation.json"); //default hoverfly resource path which is src/test/resources/hoverfly
    SimulationSource.url("http://www.my-service.com/simulation.json"); // URL
    SimulationSource.url(new URL("http://www.my-service.com/simulation.json")); // URL
    SimulationSource.file(Paths.get("src", "simulation.json")); // File
    SimulationSource.dsl(service("www.foo.com").get("/bar).willReturn(success())); // Object
    SimulationSource.simulation(new Simulation()); // Object
    SimulationSource.empty(); // None

You can pass in multiple sources when importing simulations, for instance, if you need to combine simulations from previous capture session and
ones that created via DSL:

.. code-block:: java

    hoverfly.simulate(
                classpath("test-service.json"),
                dsl(service("www.my-test.com")
                        .post("/api/bookings").body("{\"flightId\": \"1\"}")
                        .willReturn(created("http://localhost/api/bookings/1")))
        );
