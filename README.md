# worker queue sample

This code sample written in Clojure runs multiple threads ("jobs") as items ("resources") are added to a queue. Once the job finishes,  the resource is inserted into the queue again, and the cycle repeats.  The max number of running jobs equals the total number of resources available.


Some delays were added in a few different spots so that it the output that is printed to the screen can be viewed.


## Usage

FIXME

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
