# Revision info #
* $HeadURL: https://cscs-repast-demos.googlecode.com/svn/richard/StupidModel/tags/2011_06_18_model_16/TODO.md $
* $LastChangedDate: 2011-06-16 16:16:52 +0200 (Cs, 16 jún. 2011) $
* $LastChangedRevision: 379 $
* $LastChangedBy: richard.legendi@gmail.com $
* $Id: TODO.md 379 2011-06-16 14:16:52Z richard.legendi@gmail.com $

# TODO #
This file contains some minor-major current TODO elements.

## General ##

### Possible bugs? ###
* Reinit fails for some reason sometimes: 2-3 init calls are required to display the grid, why?

## Model 4##
* How to probe ValueLayerProbeObject2D? There are 2 options here:
	* The HabitatCells may be probed. This way we have no Value Layers, but HabitatCell objects in the grid
	  The Bug's move action need a bit of update. 
	* We use value layers and see the grass growing

* Error reporting: revert everywhere to MessageCenter's functions (that is the standard Repast S way to do it)

* Commit backwards:
	* Bug#foodConsumption() from Model 3
	* Multi occuppancy from Model 3
	* Cell background should fade from black to green, not whit to green (pre-14)

### FIXMEs ###
* How to infer generic types with `StupidModelContextBuilder#init(Context)`?
