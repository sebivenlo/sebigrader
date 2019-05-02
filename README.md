# sebigrader
correct performance assessments using unit test reports

To classify the test results and thus the correction results, we apply
the following scheme:

The first letter describes the tes, the second the business code

* AA is the solution test applied to the solution business.
* BB is the candidate test on the candidate business, this is what the
  candidate is supposed to do during the performance assessment. Will
  be recorded.
* AB is the solution test on the candidate business, grades the
  candidat business
* B0 is the candidate test on a trivially brolen solution, in the 0
  case the start project.
* B1 is the candidate test on a broken implementation.
* B2 is the candidate test on a broken implementation with flaws that
  are not combinable in B0 or B1


The test runs B1..Bn will exists for those cases where there are quite
som situations to consider. B1..Bn may to differ in only a few spots.

The color schem for the ideal situation:

AA is green
BB is green
B0..Bn has reds, such that all tests have been red at least once in
any of B0..Bn.

