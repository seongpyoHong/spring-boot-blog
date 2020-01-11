resource "aws_key_pair" "web_admin" {
    key_name = "web_admin"
    public_key = "${file("~/.ssh/web_admin.pub")}"
}

resource "aws_security_group" "mysql" {
  name = "allow_mysql_port_from_all"
  description = "Allow mysql port from all"
  ingress {
    from_port = 3306
    to_port = 3306
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
resource "aws_security_group" "redis" {
  name = "allow_redis_port_from_all"
  description = "Allow redis port from all"
  ingress {
    from_port = 6379
    to_port = 6379
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ssh" {
  name = "allow_ssh_from_all"
  description = "Allow SSH port from all"
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
resource "aws_security_group" "https" {
  name = "allow_https_from_all"
  description = "Allow HTTPS port from all"
  ingress {
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
resource "aws_security_group" "http" {
  name = "allow_http_from_all"
  description = "Allow HTTP port from all"
  ingress {
    from_port = 80
    to_port = 80
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

data "aws_security_group" "default" {
  name = "default"
}

resource "aws_instance" "spring_server" {
  ami = "ami-e21cc38c" # Amazon Linux AMI 2017.03.1 Seoul
  instance_type = "t2.micro"
  key_name = "${aws_key_pair.web_admin.key_name}"
  vpc_security_group_ids = [
    "${aws_security_group.http.id}",
    "${aws_security_group.https.id}",
    "${aws_security_group.ssh.id}",
    "${aws_security_group.redis.id}",
    "${data.aws_security_group.default.id}"
  ]
  root_block_device {
      volume_size = 30
  }
}

resource "aws_eip" "server_eip" {
    vpc = true
    instance = "${aws_instance.spring_server.id}"
}

resource "aws_db_instance" "springbootwebservice_db" {
    allocated_storage = 20
    engine = "mariadb"
    engine_version = "10.2.11"
    instance_class = "db.t2.micro"
    username = user
    password = password
    publicly_accessible = true
    skip_final_snapshot = true
    vpc_security_group_ids = [
    "${aws_security_group.mysql.id}",
    "${data.aws_security_group.default.id}"
    ]
}