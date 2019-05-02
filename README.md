# sebigrader
Correct performance assessments using unit test reports.

To classify the test results and thus the correction results, we apply
the following scheme:

The first letter describes the tes, the second the business code

* AA is the solution test applied to the solution business.
* BB is the candidate test on the candidate business, this is what the
  candidate is supposed to do during the performance assessment. Will
  be recorded.
* AB is the solution test on the candidate business, grades the
  candidate business.
* BA is the candidate test on the solution business, grades the candidate tests, as do the combinations below.
* B0 is the candidate test on a trivially broken solution, in the 0
  case the start project, with solution part stripped.
* B1 is the candidate test on a broken implementation.
* B2 is the candidate test on a broken implementation with flaws that
  are not combinable in B0 or B1,
* etc...


The test runs B1..Bn will exists for those cases where there are quite
some situations to consider. The solutions 1..n used in B1..Bn typically differ in only a few spots.

The colour scheme for the ideal situation:

* AA is green: this is the baseline.
* BB is green: candidate worked well.
* BA is green: candidate agrees with solution.
* AB is green: we agree with candidate's soultion.
* B0..Bn has reds, such that all tests have been red at least once in
  any of B0..Bn.

