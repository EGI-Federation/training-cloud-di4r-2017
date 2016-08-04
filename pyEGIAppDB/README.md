# pyEGIAppDB
pyEGIAppDB is an open-source Python script to gather from the <a href="https://appdb.egi.eu/">EGI Application Database</a> the list of cloud providers that have subscribed a given VO. 

For each provider the available Virtual Appliances and resource template will be shown.

## Compile and Run

Edit the preferences in the Python script `pyEGIAppDB.py`:
```
[..]
vo = "training.egi.eu"; // <= Change here!
```

Run (you may redirect the output to a file):
```
$ python pyEGI_AppDB.py
```

## Dependencies

pyEGIAppDB uses:
- httplib
- xmltodict
