.. _diffing:

Diffing
=======

It is possible to set Hoverfly in Diff mode to detect the differences between a simulation and the the actual requests and responses.
When Hoverfly is in Diff mode, it forwards the requests and serves responses from real service, and at the meantime, generates a diff
report that stores in memory. You can later on call the ``assertThatNoDiffIsReported`` function to verify if any discrepancy is detected.

.. code-block:: java

    try(Hoverfly hoverfly = new Hoverfly(configs(), DIFF)) {

        hoverfly.start();
        hoverfly.importSimulation(classpath("simulation-to-compare.json"));

        // do some requests here

        hoverfly.assertThatNoDiffIsReported(false);

    }

If you pass ``true`` to ``assertThatNoDiffIsReported``, it will instruct Hoverfly to reset the diff logs after the assertion.