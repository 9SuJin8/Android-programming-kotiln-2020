import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

_tile(context, icon, title) => ListTile(
  leading: Icon(icon),
  title: Text(title),
  subtitle: Text('subtitle $title'),
  onTap: () => showCustomDialog(context, "Person", title),
);

class _ScaffoldBody extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var list = <Widget>[];
    for (var i = 0; i < 20; i++) {
      list.add(_tile(context, Icons.access_alarm, "Person $i"));
    }
    return ListView(
      children: list,
    );
  }
}

class ListPage extends StatelessWidget {
  static const nav_url = '/list_view';
  static const menu_name = 'ListView Page';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text(menu_name)),
        body: _ScaffoldBody()
    );
  }
}

Future<void> showCustomDialog(context, title, msg) {
  return showDialog(
    context: context,
    barrierDismissible: false, // user must tap button!
    builder: (BuildContext context) {
      return AlertDialog(
        title: Text(title),
        content: Text(msg),
        actions: [
          RaisedButton(
            child: Text('OK'),
            onPressed: () {
              Navigator.of(context).pop();
            },
          ),
        ],
      );
    },
  );
}

void showCustomSnackBar(context, String msg) {
  Scaffold.of(context).showSnackBar(
      SnackBar(content: Text(msg)));
}


class AppState extends ChangeNotifier {
  int state = 0;

  AppState() {
    SharedPreferences.getInstance().then((prefs) {
      state = prefs.getInt('state');
    }
    );
  }

  void increaseState() {
    state++;
    SharedPreferences.getInstance().then((prefs) =>
    prefs.setInt('state', state));
    notifyListeners();
  }
}

class StartPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Start Page')),
      body: Center(
          child: RaisedButton(
            child: Text('Go to NextPage'),
            onPressed: () {
              Navigator.pushNamed(context, ListPage.nav_url);
            }
          )
      ),
    );
  }
}

class NextPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Next Page')),
      body: Center(
        child:Consumer<AppState>(
          builder: (context, appState, child) => RaisedButton(
            child: Text('Go Back ${appState.state}'),
            onPressed: () => Navigator.pop(context),
          )
      ),
      )
    );
  }
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      title: 'test',
      initialRoute: '/',
      routes: {
        '/': (context) => StartPage(),
        '/next': (context) => NextPage(),
        ListPage.nav_url: (context) => ListPage()
      },
    );
  }
}

void main() => runApp(
  ChangeNotifierProvider(
      create: (context) => AppState(),
  child: MyApp()));