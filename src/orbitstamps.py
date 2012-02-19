import BaseHTTPServer
from SimpleHTTPServer import SimpleHTTPRequestHandler

def HttpRequestHandler(SimpleHTTPRequestHandler):
	
	def __init__(self,name):
		self.wwwroot = "www"
		

def main():
	HandlerClass = SimpleHTTPRequestHandler
	Protocol     = "HTTP/1.0"
	HandlerClass.protocol_version = Protocol
	
	httpd = BaseHTTPServer.HTTPServer(('', 8000), HandlerClass)
	sa = httpd.socket.getsockname()
	print "Serving HTTP on", sa[0], "port", sa[1], "..."
	httpd.serve_forever()

if __name__ == "__main__":
    main()
